package dev.ime.tool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access= AccessLevel.PRIVATE )
public class RegexPattern {

	public static final String NAME_BASIC = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.,&:]+";
	public static final String NAME_FULL = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.&,:]{0,50}";
	public static final String DESCRIPTION_FULL = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s\\-\\?\\¿\\!\\¡\\.&,:]{0,100}";
}
