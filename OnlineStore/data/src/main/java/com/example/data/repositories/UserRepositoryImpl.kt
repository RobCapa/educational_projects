package com.example.data.repositories

import com.example.data.database.dao.UserDao
import com.example.data.models.DUser
import com.example.data.utils.DataDomainConverter
import com.example.domain.models.User
import com.example.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun saveUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            val dUser = DUser(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                phoneNumber = user.phoneNumber,
            )
            userDao.addUser(dUser)

            true
        }
    }

    override suspend fun removeUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            val dUser = DataDomainConverter.convertUserToDUser(user)
            userDao.removeUser(user = dUser)

            true
        }
    }

    override suspend fun getUser(id: String): User? {
        return withContext(Dispatchers.IO) {
            val dbUser = userDao.getUser(id) ?: return@withContext null

            User(
                id = dbUser.id,
                firstName = dbUser.firstName,
                lastName = dbUser.lastName,
                phoneNumber = dbUser.phoneNumber,
            )
        }
    }
}