package org.novi.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface FlagRepository : JpaRepository<Flag, Long>