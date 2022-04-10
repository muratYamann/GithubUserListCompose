package com.trt.international.core

import com.trt.international.core.local.db.entity.UserFavoriteEntity
import com.trt.international.core.model.UserDetail
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.responses.UserDetailResponse
import com.trt.international.core.responses.UserSearchResponseItem
import com.trt.international.core.responses.users.DiscoverUsersResponse

object DataMapper {


    fun mapUserSearchResponseToDomain(data: List<UserSearchResponseItem>): List<UserSearchItem> =
        data.map {
            UserSearchItem(
                avatarUrl = it.avatarUrl,
                id = it.id,
                login = it.login,
            )
        }

    fun mapDiscoverUsersResponseToDomain(data: DiscoverUsersResponse): List<UserSearchItem> =
        data.map {
            UserSearchItem(
                avatarUrl = it.avatar_url,
                id = it.id,
                login = it.login,
            )
        }

    fun mapUserDetailResponseToDomain(data: UserDetailResponse): UserDetail =
        UserDetail(
            username = data.login.toString(),
            name = data.name,
            avatarUrl = data.avatarUrl,
            followersUrl = data.followersUrl,
            bio = data.bio,
            company = data.company,
            publicRepos = data.publicRepos,
            followingUrl = data.followingUrl,
            followers = data.followers,
            following = data.following,
            location = data.location,
            email = data.email
        )

    fun mapUserFavoriteEntitiesToDomain(data: List<UserFavoriteEntity>): List<UserFavorite> =
        data.map {
            UserFavorite(
                username = it.username,
                name = it.name,
                avatarUrl = it.avatarUrl,
                followersUrl = it.followersUrl,
                bio = it.bio,
                company = it.company,
                publicRepos = it.publicRepos,
                followingUrl = it.followingUrl,
                followers = it.followers,
                following = it.following,
                location = it.location
            )
        }

    fun mapUserFavoriteDomainToEntity(data: UserFavorite): UserFavoriteEntity =
        UserFavoriteEntity(
            username = data.username,
            name = data.name,
            avatarUrl = data.avatarUrl,
            followersUrl = data.followersUrl,
            bio = data.bio,
            company = data.company,
            publicRepos = data.publicRepos,
            followingUrl = data.followingUrl,
            followers = data.followers,
            following = data.following,
            location = data.location
        )

    fun mapUserDetailToUserFavorite(it: UserDetail): UserFavorite =
        UserFavorite(
            username = it.username,
            name = it.name,
            avatarUrl = it.avatarUrl,
            followersUrl = it.followersUrl,
            bio = it.bio,
            company = it.company,
            publicRepos = it.publicRepos,
            followingUrl = it.followingUrl,
            followers = it.followers,
            following = it.following,
            location = it.location
        )

    fun mapUserSearchItemToUserFavorite(it: UserSearchItem): UserFavorite =
        UserFavorite(
            username = it.login!!,
            name = "",
            avatarUrl = it.avatarUrl,
            followersUrl = "",
            bio = "",
            company = "",
            publicRepos = 0,
            followingUrl = "",
            followers = 0,
            following = 0,
            location = ""
        )

}