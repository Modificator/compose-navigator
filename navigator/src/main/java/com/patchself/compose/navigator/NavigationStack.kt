package com.patchself.compose.navigator

class NavigationStack {
    var currentIndex = -1
        private set
    private val stack = arrayListOf<PageController>()


    fun set(items: Array<PageController>) {
        stack.clear()
        currentIndex = items.size - 1
        stack.addAll(items)
    }

    fun insert(item: PageController, index: Int) {
        if (index <= currentIndex) {
            stack.add(index, item)
            currentIndex++
        }
    }

    fun push(page: PageController, setAsCurrent: Boolean) {
        stack.add(page)
        if (setAsCurrent) {
            currentIndex++
        }
    }

    fun indexOf(item: PageController): Int {
        var index = 0
        val it = stack.iterator()
        while (it.hasNext()) {
            if (item == it.next()) {
                return index
            }
            index++
        }
        return -1
    }

    fun isEmpty() = stack.isEmpty()

    fun size() = stack.size

    fun get(index: Int) = if (index < 0 || index >= stack.size) null else stack[index]

    fun getAll() = stack

    fun getCurrent() =
        if (currentIndex == -1 || currentIndex >= stack.size) null else stack[currentIndex]

    fun getPrevious() = get(currentIndex - 1)

    fun findLastById(id: Int): PageController? {
        for (i in stack.indices.reversed()) {
            val page = stack[i]
            if (page.getId() == id) {
                return page
            }
        }
        return null
    }

    fun remove(index: Int): PageController? {
        return if (index < 0 || index >= stack.size || index == currentIndex) {
            null
        } else if (index >= currentIndex) {
            stack.removeAt(index)
        } else {
            currentIndex--
            stack.removeAt(index)
        }
    }

    fun removeLast(): PageController? {
        if (stack.isEmpty()) {
            return null
        }
        val page = stack.removeAt(currentIndex)
        currentIndex--
        return page
    }

    fun removeById(id: Int): PageController? {
        var index = 0
        val it = stack.iterator()
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                return remove(index)
            }
            index++
        }
        return null
    }

    fun destroy(index: Int): PageController? {
        val page = remove(index)
        page?.destory()
        return page
    }

    fun destoryById(id: Int): PageController? {
        var index = 0
        val it = stack.iterator()
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                return destroy(index)
            }
            index++
        }
        return null
    }

    fun destoryAllById(id: Int) {
        for (index in stack.size - 2 downTo 0) {
            if (stack.getOrNull(index)?.getId() == id) {
                destroy(index)
            }
        }
    }

    fun destroyByIdExcludingLast(id: Int): PageController? {
        var index = 0
        val it = stack.iterator()
        while (it.hasNext()) {
            if (it.next().getId() == id && currentIndex != index) {
                return destroy(index)
            }
            index++
        }
        return null
    }

    fun clear() {
        if (stack.size > 0) {
            val it = stack.iterator()
            while (it.hasNext()) {
                val page = it.next()
                page.destory()
            }
            stack.clear()
            currentIndex = -1
        }
    }

    fun reset(saveFirst: Boolean) {
        val last = removeLast()!!
        if (!saveFirst) {
            clear()
        } else {
            val first = stack[0]
            var index = 0
            val it = stack.iterator()
            while (it.hasNext()) {
                val page = it.next()
                if (index != 0) {
                    page.destory()
                }
                index++
            }
            stack.clear()
            stack.add(first)
        }
        push(last, true)
    }

    fun resetSilently(initial: PageController) {
        stack.clear()
        stack.add(initial)
        currentIndex = 0
    }
}

