object Compressor {

    interface ICompressor {
        fun compress(text: String): String
        fun decompress(binaryString: String): String
    }

    fun with(codeTable: List<Pair<Char, String>>): ICompressor {
        return object : ICompressor {

            override fun compress(text: String): String {
                val builder = StringBuilder()
                text.forEach { char ->
                    val code = codeTable.find { char == it.first }?.second
                    code?.let {
                        builder.append(it)
                    } ?: IllegalArgumentException("Character not present in code table: $char")
                }
                return builder.toString()
            }

            override fun decompress(binaryString: String): String {

                val sortedCodeTable = codeTable.sortedByDescending { it.second.length }
                val bitList = binaryString.toCharArray().toMutableList()

                val builder = StringBuilder()

                while (bitList.size > 0) {

                    val matched = sortedCodeTable.firstOrNull {
                        it.second == bitList.take(it.second.length).joinToString("")
                    }

                    matched?.let {
                        builder.append(it.first)
                        repeat(it.second.length) { bitList.removeAt(0) }
                    } ?: IllegalArgumentException("Unrecognized code")
                }
                return builder.toString()
            }
        }
    }

}