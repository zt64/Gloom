package com.materiapps.gloom.domain.repository

import com.materiapps.gloom.domain.models.ModelUser
import com.materiapps.gloom.rest.service.GraphQLService
import com.materiapps.gloom.rest.utils.transform

class GraphQLRepository(
    private val service: GraphQLService
) {

    suspend fun getCurrentProfile() =
        service.getCurrentProfile().transform { ModelUser.fromProfileQuery(it) }

    suspend fun getProfile(username: String) =
        service.getProfile(username).transform { ModelUser.fromUserProfileQuery(it) }

    suspend fun getRepositoriesForUser(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getRepositoriesForUser(username, after, count)

    suspend fun getStarredRepositories(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getStarredRepositories(username, after, count)

    suspend fun getJoinedOrgs(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getJoinedOrgs(username, after, count)

    suspend fun getFollowers(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getFollowers(username, after, count)

    suspend fun getFollowing(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getFollowing(username, after, count)

    suspend fun getSponsoring(
        username: String,
        after: String? = null,
        count: Int? = null
    ) = service.getSponsoring(username, after, count)

    suspend fun followUser(id: String) =
        service.followUser(id).transform { it.followUser?.user?.viewerIsFollowing }

    suspend fun unfollowUser(id: String) =
        service.unfollowUser(id).transform { it.unfollowUser?.user?.viewerIsFollowing }

}