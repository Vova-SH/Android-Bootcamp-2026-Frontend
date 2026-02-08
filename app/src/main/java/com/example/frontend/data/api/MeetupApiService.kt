package com.example.frontend.data.api

import com.example.frontend.data.model.*
import retrofit2.http.*

interface MeetupApiService {
    
    @GET("api/person")
    suspend fun getAllPersons(): List<PersonWithInvitesDTO>
    
    @GET("api/person/paginated")
    suspend fun getAllPersonsPaginated(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PagedModelPersonWithInvitesDTO
    
    @GET("api/person/{id}")
    suspend fun getPersonById(@Path("id") id: Long): PersonWithInvitesDTO
    
    @GET("api/person/login/{login}")
    suspend fun getByLogin(@Path("login") login: String): String
    
    @GET("api/person/login")
    suspend fun loginPerson(): PersonWithInvitesDTO
    
    @POST("api/person/register")
    suspend fun createPerson(@Body person: PersonRegisterDTO): PersonShortDTO
    
    @PUT("api/person/{id}")
    suspend fun updatePerson(
        @Path("id") id: Long,
        @Body person: PersonShortDTO
    ): PersonWithInvitesDTO
    
    @DELETE("api/person/{id}")
    suspend fun deletePerson(@Path("id") id: Long)
    
    @GET("api/meetup")
    suspend fun getAllMeetups(): List<MeetupWithInvitesDTO>

    @GET("api/meetup")
    suspend fun getAllPersonsMeetups(@Query("id") id: Long = 0): List<MeetupWithInvitesDTO>

    @GET("api/meetup/{id}")
    suspend fun getMeetupById(@Path("id") id: Long): MeetupWithInvitesDTO
    
    @POST("api/meetup")
    suspend fun createMeetup(@Body meetup: MeetupToCreateDTO): MeetupDTO
    
    @PUT("api/meetup/{id}")
    suspend fun updateMeetup(
        @Path("id") id: Long,
        @Body meetup: MeetupShortDTO
    ): MeetupShortDTO
    
    @DELETE("api/meetup/{id}")
    suspend fun deleteMeetup(@Path("id") id: Long)
    
    @GET("api/invites")
    suspend fun getAllInvites(): List<InviteDTO>
    
    @POST("api/invites")
    suspend fun createInvite(@Body invite: InviteCreateDTO): InviteDTO
    
    @PUT("api/invites/{id}")
    suspend fun updateInvite(
        @Path("id") id: Long,
        @Body invite: InviteDTO
    ): InviteDTO
    
    @GET("api/department")
    suspend fun getAllDepartments(): List<DepartmentDTO>
    
    @GET("api/department/{id}")
    suspend fun getDepartmentById(@Path("id") id: Long): DepartmentDTO
    
    @POST("api/department")
    suspend fun createDepartment(@Body department: DepartmentDTO): DepartmentDTO
    
    @PUT("api/department/{id}")
    suspend fun updateDepartment(
        @Path("id") id: Long,
        @Body department: DepartmentDTO
    ): DepartmentDTO
    
    @DELETE("api/department/{id}")
    suspend fun deleteDepartment(@Path("id") id: Long)
}
