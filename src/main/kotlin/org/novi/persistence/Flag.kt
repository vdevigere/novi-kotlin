package org.novi.persistence

import jakarta.persistence.*
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "flag")
data class Flag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(unique = true)
    val name: String,

    @Transient
    var status: Boolean = false,

    @ManyToMany(cascade = [CascadeType.ALL])
    val activationConfigs: Set<ActivationConfig>
) {
    companion object {
        val EMPTY: Flag = Flag(null, "Null", false, Collections.emptySet())
    }
}