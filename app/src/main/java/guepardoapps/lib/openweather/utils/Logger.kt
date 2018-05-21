package guepardoapps.lib.openweather.utils

import android.os.Environment
import java.io.File
import java.util.*

class Logger private constructor() {
    private val MaxLogFileLines: Int = 65532

    private var _documentDir: File? = null
    private var _logFile: File? = null

    private var _loggingEnabled: Boolean = true
    private var _writeToFileEnabled: Boolean = false
    private var _writeToDatabaseEnabled: Boolean = false

    init {
        _documentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        checkIfLogFileExists()
    }

    private object Holder {
        val instance: Logger = Logger()
    }

    companion object {
        val instance: Logger by lazy { Holder.instance }
    }

    fun setLoggingEnabled(loggingEnabled: Boolean) {
        _loggingEnabled = loggingEnabled
    }

    fun isLoggingEnabled(): Boolean {
        return _loggingEnabled
    }

    fun setWriteToFileEnabled(writeToFileEnabled: Boolean) {
        _writeToFileEnabled = writeToFileEnabled
        // TODO check if file and dir exists and create if not
    }

    fun isWriteToFileEnabled(): Boolean {
        return _writeToFileEnabled
    }

    fun setWriteToDatabaseEnabled(writeToDatabaseEnabled: Boolean) {
        _writeToDatabaseEnabled = writeToDatabaseEnabled
    }

    fun isWriteToDatabaseEnabled(): Boolean {
        return _writeToDatabaseEnabled
    }

    private fun checkIfLogFileExists(): Boolean {
        return false
    }

    private fun createFileName(): String {
        return String.format(Locale.getDefault(), "LucaHome-Log-%s.txt",
                getCurrentDateTimeString())
    }

    private fun getCurrentDateTimeString(): String {
        val now: Calendar = Calendar.getInstance()
        return String.format(Locale.getDefault(), "%04d.%02d.%02d-%02d.%02d",
                now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH),
                now.get(Calendar.HOUR), now.get(Calendar.MINUTE))
    }
}