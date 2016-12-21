package utils

import play.api.libs.Codecs

object StringUtils {
	val md = java.security.MessageDigest.getInstance("SHA-1")

	def sha1(text: String) = {
		Codecs.sha1(md.digest(text.getBytes))
	}
}