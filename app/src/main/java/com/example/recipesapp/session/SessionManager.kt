import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFERENCES_NAME = "UserSession"
    private const val KEY_EMAIL = "email"
    private const val KEY_USERNAME = "username"

    fun saveUserSession(context: Context, email: String, username: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    // Actualizar solo el username de la sesión
    fun updateUsernameInSession(context: Context, newUsername: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, newUsername)  // Solo actualizamos el username
        editor.apply()  // Aplicamos los cambios
    }

    fun getUserSession(context: Context): Pair<String?, String?> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        return Pair(email, username)
    }

    fun clearSession(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
