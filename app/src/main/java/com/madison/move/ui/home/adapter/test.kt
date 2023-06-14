package com.madison.move.ui.home.adapter

fun main() {
    val array1 = mutableListOf<Int>(1, 2, 3, 4, 5)
    val array2 = mutableListOf(4, 5, 6, 7, 8)

    val mergedArray = array1.union(array2).toTypedArray() // Chuyển kết quả về một mảng

    println(mergedArray.contentToString()) // In ra mảng gộp

    // Hoặc nếu bạn muốn giữ lại các phần tử trùng nhau
    val duplicatedArray = array1.toMutableList()
    duplicatedArray.addAll(array2)

    println(duplicatedArray) // In ra mảng gộp
}
