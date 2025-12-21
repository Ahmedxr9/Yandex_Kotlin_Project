package com.around_team.todolist.data.db

import androidx.room.Transaction
import com.around_team.todolist.ui.common.models.TodoItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository class for managing Todo items in the database.
 *
 * @property dao The Data Access Object (DAO) used to interact with the database.
 */
class DatabaseRepository @Inject constructor(
    private val dao: Dao,
) {

    /**
     * Inserts a single todo item into the database.
     *
     * @param todo The todo item to insert.
     */
    suspend fun insertTodo(todo: TodoItem) = dao.insertTodo(todo.toEntity())

    /**
     * Retrieves all todo items from the database.
     *
     * @return A list of all todo items.
     */
    suspend fun getAllTodos(): List<TodoItem> {
        return dao
            .getAllTodos()
            .map { it.toTodoItem() }
    }

    /**
     * Observes all todo items as a Flow that emits whenever the underlying table changes.
     */
    fun observeAllTodos(): Flow<List<TodoItem>> {
        return dao
            .observeAllTodos()
            .map { list -> list.map { it.toTodoItem() } }
    }

    /**
     * Retrieves a single todo item from the database by its ID.
     *
     * @param todoId The unique identifier of the todo item.
     * @return The matching todo item, or null if not found.
     */
    suspend fun getTodoById(todoId: String): TodoItem? {
        return dao.getTodoById(todoId)?.toTodoItem()
    }

    /**
     * Deletes a todo item from the database by its ID.
     *
     * @param todoId The unique identifier of the todo item to delete.
     */
    suspend fun deleteTodoById(todoId: String) = dao.deleteTodoById(todoId)

    /**
     * Replaces all todo items in the database with the provided list.
     * This is done within a transaction to ensure atomicity.
     *
     * @param todos The list of todo items to insert.
     */
    @Transaction
    suspend fun replaceTodos(todos: List<TodoItem>) {
        dao.deleteAllTodos()
        dao.insertTodos(todos.map { it.toEntity() })
    }
}
