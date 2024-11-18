package com.tomakeitgo

class R {
    companion object {
        fun readFile(path: String): String {
            return R::class.java.getResource(path)?.readText() ?: ""
        }

        fun fileLinesAsSequence(path: String): Sequence<String> {
            return readFile(path).splitToSequence("[\n\r]".toRegex())
        }
    }
}