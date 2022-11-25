package com.epifoos.domain.user.profile

import com.epifoos.domain.user.User
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object AvatarService {

    private const val CATEGORY_FILE = "category.txt"
    private const val UNKNOWN_CATEGORY = "Unknown"

    fun getAvailableAvatars(user: User): Map<String, List<String>> {
        return transaction {
            Avatar.find { AvatarTable.available eq true }
                .toList()
                .groupBy({ it.category }, { it.path })
        }
    }

    fun updateAvatar(user: User, avatar: String) {
        transaction {
            user.avatar = avatar
        }
    }

    fun populateAvatars() {
        val avatarMap = mutableMapOf<String, List<String>>()
        Files.walk(Paths.get("assets/avatars/"), 1).forEach {
            if (Files.exists(Paths.get("${it}/${CATEGORY_FILE}"))) {
                avatarMap[getCategory(it)] = getAvatarsInFolder(it)
            }
        }

        createAvatars(avatarMap)
    }

    private fun createAvatars(avatarMap: Map<String, List<String>>) {
        transaction {
            AvatarTable.deleteAll()

            avatarMap.forEach { entry ->
                entry.value.forEach {
                    Avatar.new {
                        category = entry.key
                        path = it
                        available = true
                    }
                }
            }
        }
    }

    private fun getAvatarsInFolder(path: Path): List<String> {
        val avatars = mutableListOf<String>()

        Files.walk(path).forEach {
            if (Files.isRegularFile(it) && it.fileName.toString() != CATEGORY_FILE) {
                avatars.add(it.toString())
            }
        }
        return avatars
    }

    private fun getCategory(path: Path): String {
        val lines = File("${path}/${CATEGORY_FILE}").useLines { it.toList() }
        return lines.firstOrNull()?.trim() ?: UNKNOWN_CATEGORY
    }
}
