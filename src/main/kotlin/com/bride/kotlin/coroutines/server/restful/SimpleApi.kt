package com.bride.kotlin.coroutines.server.restful

import com.bride.kotlin.coroutines.server.repo.Student
import com.bride.kotlin.coroutines.server.repo.StudentRepository
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/students")
class SimpleApi(val repository: StudentRepository) {
    @GetMapping("/")
    fun listStudents() = repository.findAll().asFlow()

    @GetMapping("/id/{id}")
    suspend fun getById(@PathVariable("id") id: Long): Student = repository.findById(id).awaitSingle()

    @GetMapping("/name/{name}")
    suspend fun getByName(@PathVariable("name") name: String): Student = repository.findByName(name).awaitFirst()
}