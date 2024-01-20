package org.novi.persistence

import jakarta.persistence.*

@Entity
@Table(name = "activation_config")
data class ActivationConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String,

    @Column(columnDefinition = "TEXT")
    val config: String
)
