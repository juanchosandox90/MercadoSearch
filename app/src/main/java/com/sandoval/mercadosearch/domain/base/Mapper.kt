package com.sandoval.mercadosearch.domain.base

interface Mapper<I, O> {
    fun map(input: I): O
}