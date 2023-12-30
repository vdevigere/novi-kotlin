package org.novi.persistence

import org.springframework.data.repository.CrudRepository

interface FlagRepository : CrudRepository<Flag, Long> {
}