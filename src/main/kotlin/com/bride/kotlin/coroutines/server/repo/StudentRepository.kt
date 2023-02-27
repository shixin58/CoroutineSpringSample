package com.bride.kotlin.coroutines.server.repo

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

@Table("student")
data class Student(
    @Column("name") val name: String,
    @Column("score") val score: Int,
    @Id val id: Long? = null// 值由数据库自动生成，自增
)

// 用来查询数据库，类似DAO
interface StudentRepository : ReactiveCrudRepository<Student,Long> {
    // 此处不能混淆，服务端代码没必要混淆
    @Query("select * from student where name = :name")
    fun findByName(name: String): Mono<Student>// Mono是webflux提供的一个Reactive实现，类似RxJava里的Single
}