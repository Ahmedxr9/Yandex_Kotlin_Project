package com.around_team.todolist.data.network

/**
 * Configuration enum for Todo list related endpoints and addresses.
 *
 * @property value The string value representing the endpoint or address.
 */
enum class TodoListConfig(private val value: String) {
    HOST_ADDRESS("https://hive.mrdekk.ru/todo"),
    LIST_ADDRESS("$HOST_ADDRESS/list"),
    AUTH_ADDRESS("$HOST_ADDRESS/auth"); // Authentication endpoint

    override fun toString(): String = value

    companion object {
        fun getElementListAddress(id: String): String = "$LIST_ADDRESS/$id"

        // Yandex OAuth Configuration (Client ID is used in AndroidManifest.xml)
        // NOTE: Client Secret should ONLY be used on a secure backend server, not in client-side code.
        // It's included here for documentation purposes as per user request, but should be removed
        // from client-side code in a production environment.
        const val YANDEX_CLIENT_ID = "cadb97ed49fb4cf18569847637bcf142"
        const val YANDEX_CLIENT_SECRET = "4a297e38bfbb4f74976717fe6f4e4b34"

        // Google OAuth Configuration
        // NOTE: This is an Android Client ID (not Web Client ID)
        // Android Client IDs:
        // - Are configured in Google Cloud Console with package name: com.around_team.todolist
        // - Require SHA-1 and SHA-256 fingerprints to be registered
        // - CANNOT issue ID tokens (requestIdToken() will fail)
        // - Only provide: email, account ID, display name, photo URL
        // - Used for basic Google Sign-In without server-side token verification
        // 
        // If you need ID tokens for server-side verification, create a Web Client ID
        // and use it with .requestIdToken() in GoogleSignInOptions
        const val GOOGLE_CLIENT_ID = "389522785814-tggn6h2d6irj7jp2ncgv14egajrcv8rv.apps.googleusercontent.com"

        // Default values
        const val DEFAULT_LAST_UPDATED_BY = "1"
        const val DEFAULT_VERSION_NAME = "1.0"
    }
}