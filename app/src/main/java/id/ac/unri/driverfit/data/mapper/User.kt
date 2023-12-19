package id.ac.unri.driverfit.data.mapper

import id.ac.unri.driverfit.data.local.Entity.UserEntity
import id.ac.unri.driverfit.data.remote.payload.UserResponse
import id.ac.unri.driverfit.domain.model.User

fun UserResponse.asModel(): User {
    return User(
        id = id,
        name = name ?: "",
        email = email,
        photo = null,
        token = token
    )
}

fun UserResponse.asEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name ?: "",
        email = email,
        photo = null,
        token = token
    )
}

fun UserEntity.asModel(): User {
    return User(
        id = id,
        name = name,
        email = email,
        photo = photo,
        token = token
    )
}
