package com.epifoos.exceptions

import io.konform.validation.ValidationErrors

class ValidationException(errors: ValidationErrors) : BaseException() {
}
