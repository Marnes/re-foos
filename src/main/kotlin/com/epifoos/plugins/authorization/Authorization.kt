package com.epifoos.plugins.authorization

import com.epifoos.domain.user.User
import com.epifoos.exceptions.AuthenticationException
import com.epifoos.exceptions.AuthorizationException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

class AuthConfig {
    lateinit var isAdmin: (user: User) -> Boolean
}

val Authorization = createRouteScopedPlugin(
    name = "AuthorizationPlugin",
    createConfiguration = ::AuthConfig
) {
    val isAdmin = pluginConfig.isAdmin
    on(AuthenticationChecked) { call ->
        val user = call.principal<User>() ?: throw AuthenticationException(message = "Unauthenticated User")
        val admin = isAdmin(user)

        if (!admin) {
            throw AuthorizationException("User is not an Admin")
        }
    }
}

fun Route.isAdmin(build: Route.() -> Unit) = authorizedRoute(build = build)

class AuthorizedRouteSelector(private val description: String) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize ${description})"
}

private fun Route.authorizedRoute(
    build: Route.() -> Unit
): Route {
    val authorizedRoute = createChild(AuthorizedRouteSelector("Is Admin"))

    authorizedRoute.install(Authorization) {
        isAdmin = { it.admin }
    }

    authorizedRoute.build()
    return authorizedRoute
}
