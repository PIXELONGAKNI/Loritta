package com.mrpowergamerbr.loritta.utils.locale

import com.mrpowergamerbr.loritta.utils.f
import com.mrpowergamerbr.loritta.utils.loritta
import mu.KotlinLogging
import java.text.MessageFormat
import java.util.*

/**
 * Localization class, this is partly generated by the LocaleGenerator
 */
open class LegacyBaseLocale {
	companion object {
		@JvmStatic
		private val logger = KotlinLogging.logger {}

		/**
		 * Returns the Java Locale (used for dates, etc) for the specified [lcoale]
		 */
		fun toJavaLocale(locale: LegacyBaseLocale): Locale {
			val localeId = locale.getLocaleId()

			return Locale(
					when (localeId) {
						"default" -> "pt_BR"
						"pt-pt" -> "pt_PT"
						"en-us" -> "en_US"
						"es-es" -> "es_ES"
						else -> "pt_BR"
					}
			)
		}
	}

	/**
	 * Converts the current legacy locale to the new locale format
	 *
	 * This should be ONLY be used as a workaround, to help migration to the new locale system
	 */
	fun toNewLocale(): BaseLocale {
		val localeId = loritta.legacyLocales.entries.first { it.value == this }.key

		return loritta.getLocaleById(localeId)
	}

	@Transient
	@Deprecated("Please use the inner classes")
	var strings = mutableMapOf<String, String>()

	@Deprecated("Please use the inner classes")
	operator fun get(key: String, vararg arguments: Any?): String {
		if (!strings.containsKey(key)) {
			logger.warn { "Missing translation key! $key" }
			return key
		}
		return strings[key]!!.f(*arguments)
	}

	/**
	 * Gets and formats an message
	 *
	 * @param args  the arguments
	 * @param block the message
	 *
	 * @return      the formatted message
	 */
	fun <T> format(vararg args: Any?, block: LegacyBaseLocale.() -> T): T {
		val result = block.invoke(this)
		return when (result) {
			is String -> MessageFormat.format(result, *args) as T
			is List<*> -> result.map { MessageFormat.format(it.toString(), *args) } as T
			else -> throw UnsupportedOperationException("Can't parse $result in BaseLocale!")
		}
	}
}
