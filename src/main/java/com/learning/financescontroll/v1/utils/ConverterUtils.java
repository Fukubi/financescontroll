package com.learning.financescontroll.v1.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.service.ICategoryService;

public class ConverterUtils {
	
	private ConverterUtils() {}
	
	@Autowired
	private static ICategoryService categoryService;
	
	private static ModelMapper mapper = new ModelMapper();

	public static EntryDto converterEntryModelParaDto(EntryModel entry) {
		EntryDto entryDto = new EntryDto();
		entryDto.setId(entry.getId());
		entryDto.setCategoria(mapper.map(categoryService.consultar(entry.getCategoriaId()), CategoryEntity.class));
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
	
}
