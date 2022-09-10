package com.sandoval.mercadosearch.data.networking

interface Mapper<I, O> {
    fun map(input: I): O
}