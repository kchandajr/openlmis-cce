/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.cce.web.csv.recordhandler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openlmis.cce.domain.CatalogItem;
import org.openlmis.cce.repository.CatalogItemRepository;
import java.util.UUID;

public class CatalogItemPersistenceHandlerTest {

  @Captor
  private ArgumentCaptor<CatalogItem> catalogItemCaptor;

  private static final String EQCODE = "eqcode";
  public static final String SOME_MAKE = "someManufacturer";
  public static final String SOME_MODEL = "someModel";

  @Mock
  private CatalogItemRepository catalogItemRepository;

  @InjectMocks
  private CatalogItemPersistenceHandler catalogItemPersistenceHandler;

  private CatalogItem catalogItem;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    catalogItem = new CatalogItem();
    catalogItem.setEquipmentCode(EQCODE);
  }

  @Test
  public void shouldSetIdIfExistingItemFoundByCode() {
    CatalogItem existingCatalogItem = new CatalogItem();
    existingCatalogItem.setId(UUID.randomUUID());

    when(catalogItemRepository.existsByEquipmentCode(EQCODE)).thenReturn(true);
    when(catalogItemRepository.findByEquipmentCode(EQCODE)).thenReturn(existingCatalogItem);

    catalogItemPersistenceHandler.execute(catalogItem);

    verify(catalogItemRepository).save(catalogItemCaptor.capture());
    assertEquals(existingCatalogItem.getId(), catalogItemCaptor.getValue().getId());
  }

  @Test
  public void shouldSetIdIfExistingItemFoundByTypeAndModel() {
    CatalogItem existingCatalogItem = new CatalogItem();
    existingCatalogItem.setId(UUID.randomUUID());

    catalogItem.setEquipmentCode(null);
    catalogItem.setManufacturer(SOME_MAKE);
    catalogItem.setModel(SOME_MODEL);

    when(catalogItemRepository.existsByEquipmentCode(EQCODE)).thenReturn(false);
    when(catalogItemRepository.existsByManufacturerAndModel(SOME_MAKE, SOME_MODEL))
        .thenReturn(true);
    when(catalogItemRepository.findByManufacturerAndModel(SOME_MAKE, SOME_MODEL))
        .thenReturn(existingCatalogItem);

    catalogItemPersistenceHandler.execute(catalogItem);

    verify(catalogItemRepository).save(catalogItemCaptor.capture());
    assertEquals(existingCatalogItem.getId(), catalogItemCaptor.getValue().getId());
  }

  @Test
  public void shouldNotSetIdIfExistingItemNotFound() {
    when(catalogItemRepository.existsByEquipmentCode(EQCODE)).thenReturn(false);
    when(catalogItemRepository.existsByManufacturerAndModel(SOME_MAKE, SOME_MODEL))
        .thenReturn(false);

    catalogItemPersistenceHandler.execute(catalogItem);

    verify(catalogItemRepository).save(catalogItem);
  }
}