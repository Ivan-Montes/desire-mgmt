package dev.ime.tool;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TransformerTest {

	@Mock
	private Checker checker;
	@Mock
	private Logger logger;
	@InjectMocks
	private Transformer transformer;
	
	@Test
	void Transformer_fromStringToDateTimeOnErrorNull_ReturnLocalDate() {
		
		Mockito.when(checker.localDateFormat(Mockito.anyString())).thenReturn(true);
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(true);
		
		LocalDate localDate = transformer.fromStringToDateTimeOnErrorNull("1850-12-01");
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(localDate).isNotNull()
				);
	}

	@Test
	void Transformer_fromStringToDateTimeOnErrorNull_ReturnLocalDateNull() {
		
		Mockito.when(checker.localDateFormat(Mockito.anyString())).thenReturn(true);
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(true);
		
		LocalDate localDate = transformer.fromStringToDateTimeOnErrorNull("1975-08-42");
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(localDate).isNull()
				);
	}

	@Test
	void Transformer_fromStringToDateTimeOnErrorZeroes_ReturnLocalDate() {
		
		Mockito.when(checker.localDateFormat(Mockito.anyString())).thenReturn(true);
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(true);
		
		LocalDate localDate = transformer.fromStringToDateTimeOnErrorZeroes("2007-05-17");
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(localDate).isNotNull(),
				()-> Assertions.assertThat(localDate).isEqualTo(LocalDate.of(2007, 05, 17))
				);
	}

	@Test
	void Transformer_fromStringToDateTimeOnErrorZeroes_ReturnLocalDateNull() {
		
		Mockito.when(checker.localDateFormat(Mockito.anyString())).thenReturn(true);
		Mockito.when(checker.localDateValid(Mockito.anyString())).thenReturn(true);
		
		LocalDate localDate = transformer.fromStringToDateTimeOnErrorZeroes("2150-3-43");
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(localDate).isNotNull(),
				()-> Assertions.assertThat(localDate).isEqualTo(LocalDate.of(0, 1, 1))
				);
	}
}
