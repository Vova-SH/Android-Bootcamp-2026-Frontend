package com.example.frontend.data.repository

import com.example.frontend.data.api.ApiClient
import com.example.frontend.data.model.*

class MeetupRepository {
    
    private val api = ApiClient.meetupApi
    private var uid: Long = 0

    suspend fun login(): PersonWithInvitesDTO {
        return api.loginPerson()
    }

    suspend fun getAllPersons(): List<PersonWithInvitesDTO> {
        return api.getAllPersons()
    }
    
    suspend fun getCurrentPerson(): PersonWithInvitesDTO {
        if (uid == 0L) {
            throw Exception("User not authenticated")
        }
        return api.getPersonById(uid)
    }
    
    fun setCurrentUserId(id: Long) {
        uid = id
    }
    
    suspend fun getPersonById(id: Long): PersonWithInvitesDTO {
        return api.getPersonById(id)
    }
    
    suspend fun registerPerson(p: PersonRegisterDTO): PersonShortDTO {
        return api.createPerson(p)
    }
    
    suspend fun updatePerson(id: Long, p: PersonShortDTO): PersonWithInvitesDTO {
        return api.updatePerson(id, p)
    }
    
    suspend fun deletePerson(id: Long) {
        api.deletePerson(id)
    }
    
    suspend fun getAllMeetups(): List<MeetupWithInvitesDTO> {
        return api.getAllMeetups()
    }

    suspend fun getAllCurrentPersonsMeetups(id: Long): List<MeetupWithInvitesDTO> {
        return api.getAllPersonsMeetups(id)
    }
    
    suspend fun getMeetupById(id: Long): MeetupWithInvitesDTO {
        return api.getMeetupById(id)
    }
    
    suspend fun createMeetup(m: MeetupToCreateDTO): MeetupDTO {
        return api.createMeetup(m)
    }
    
    suspend fun updateMeetup(id: Long, m: MeetupShortDTO): MeetupShortDTO {
        return api.updateMeetup(id, m)
    }
    
    suspend fun deleteMeetup(id: Long) {
        api.deleteMeetup(id)
    }
    
    suspend fun createInvite(inv: InviteCreateDTO): InviteDTO {
        return api.createInvite(inv)
    }
    
    suspend fun updateInvite(id: Long, inv: InviteDTO): InviteDTO {
        return api.updateInvite(id, inv)
    }
    
    suspend fun getAllDepartments(): List<DepartmentDTO> {
        return api.getAllDepartments()
    }
    
    suspend fun getDepartmentById(id: Long): DepartmentDTO {
        return api.getDepartmentById(id)
    }
    
    suspend fun createDepartment(d: DepartmentDTO): DepartmentDTO {
        return api.createDepartment(d)
    }
    
    suspend fun updateDepartment(id: Long, d: DepartmentDTO): DepartmentDTO {
        return api.updateDepartment(id, d)
    }
    
    suspend fun deleteDepartment(id: Long) {
        api.deleteDepartment(id)
    }
}
