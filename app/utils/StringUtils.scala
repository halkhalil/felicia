package utils

import play.api.libs.Codecs
import java.security.MessageDigest

object StringUtils {
	private val md = MessageDigest.getInstance("SHA-1")

	def sha1(text: String) = {
		Codecs.sha1(md.digest(text.getBytes))
	}
}