package com.learning.financescontroll.v1.utils;

import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;

public class ConverterUtils {
	
	private ConverterUtils() {}
	

	public static EntryDto converterEntryModelParaDto(EntryModel entry) {
		EntryDto entryDto = new EntryDto();
		entryDto.setId(entry.getId());
		entryDto.setData(entry.getData());
		entryDto.setTipo(entry.getTipo());
		entryDto.setValor(entry.getValor());
		return entryDto;
	}
	
	public static EntryModel converterEntryDtoParaModel(EntryDto entry) {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(entry.getId());
		entryModel.setCategoriaId(entry.getCategoria().getId());
		entryModel.setData(entry.getData());
		entryModel.setTipo(entry.getTipo());
		entryModel.setValor(entry.getValor());
		return entryModel;
	}
	
	public static EntryModel converterEntryEntityParaModel(EntryEntity entry) {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(entry.getId());
		entryModel.setCategoriaId(entry.getCategoria().getId());
		entryModel.setData(entry.getData());
		entryModel.setTipo(entry.getTipo());
		entryModel.setValor(entry.getValor());
		return entryModel;
	}
	
}
