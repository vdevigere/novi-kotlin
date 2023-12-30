package org.novi.persistence

import jakarta.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "flag")
data class Flag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,

    @Column(unique = true)
    val name: String,

    @Transient
    val status: Boolean,

    @ManyToMany(cascade = [CascadeType.ALL])
    val activationConfigs: Set<ActivationConfig>
)