package com.bride.kotlin.coroutines.server.config

import com.bride.kotlin.coroutines.server.repo.Student
import com.bride.kotlin.coroutines.server.repo.StudentRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asPublisher
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component

// 准备数据库。Spring加载该配置文件
@Component
class ApplicationConfiguration {
    // 初始化数据库表
    // 该方法由Spring调用，StudentRepository和DatabaseClient参数由Spring自动注入
    @Bean
    fun runner(studentRepository: StudentRepository, db: DatabaseClient) = ApplicationRunner {
        val initDb = db.sql {
            """
                CREATE TABLE student (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    score Integer NOT NULL
                )
            """.trimIndent()
        }

        flowOf(
            Student("Harry", 88),
            Student("Hermione", 95),
            Student("Ron", 75),
            Student("Neville", 70)
        ).let {
            studentRepository.saveAll(it.asPublisher())
        }.let {
            // flux类似RxJava里的Observable
            initDb.then()
                .thenMany(it)
                .subscribe()
        }
    }
}