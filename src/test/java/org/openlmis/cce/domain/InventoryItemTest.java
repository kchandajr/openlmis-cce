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

package org.openlmis.cce.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Test;
import org.openlmis.cce.CatalogItemDataBuilder;
import org.openlmis.cce.InventoryItemDataBuilder;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class InventoryItemTest {

  @Test
  public void shouldReturnTrueIfStatusHasChanged() {
    InventoryItem inventoryItem = new InventoryItemDataBuilder()
        .withStatus(FunctionalStatus.NON_FUNCTIONING)
        .build();
    InventoryItem inventoryItem2 = new InventoryItemDataBuilder()
        .withStatus(FunctionalStatus.FUNCTIONING)
        .build();

    assertTrue(inventoryItem2.statusChanged(inventoryItem));
  }

  @Test
  public void shouldReturnFalseIfStatusHasNotChanged() {
    InventoryItem inventoryItem = new InventoryItemDataBuilder()
        .withStatus(FunctionalStatus.NON_FUNCTIONING)
        .build();
    InventoryItem inventoryItem2 = new InventoryItemDataBuilder()
        .withStatus(FunctionalStatus.NON_FUNCTIONING)
        .build();

    assertFalse(inventoryItem2.statusChanged(inventoryItem));
  }

  @Test
  public void shouldReturnFalseIfOldInventoryIsNull() {
    InventoryItem inventoryItem2 = new InventoryItemDataBuilder().build();

    assertFalse(inventoryItem2.statusChanged(null));
  }

  @Test
  public void shouldUpdateInventory() {
    RandomStringGenerator randomStringGenerator =
        new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();

    UUID id = UUID.randomUUID();
    UUID programId = UUID.randomUUID();
    UUID facilityId = UUID.randomUUID();
    CatalogItem catalogItemExisting = new CatalogItemDataBuilder().build();
    InventoryItem existing = new InventoryItemDataBuilder()
        .withId(id)
        .withCatalogItem(catalogItemExisting)
        .withProgramId(programId)
        .withFacilityId(facilityId)
        .build();

    CatalogItem catalogItem = new CatalogItemDataBuilder()
        .withoutFromPqsCatalogFlag()
        .withGasolineEnergySource()
        .withMinusThreeStorageTemperature()
        .withArchiveFlag()
        .build();

    String equipmentTrackingId = randomStringGenerator.generate(5);
    String referenceName = randomStringGenerator.generate(5);
    int yearOfInstallation = new Random().nextInt();
    int withYearOfWarrantyExpiry = new Random().nextInt();
    String monitorId = randomStringGenerator.generate(5);
    String additionalNotes = randomStringGenerator.generate(5);
    String firstName = randomStringGenerator.generate(5);
    String lastName = randomStringGenerator.generate(5);
    UUID lastModifierId = UUID.randomUUID();
    LocalDate decommissionDate = LocalDate.of(2010, 5, 5);
    InventoryItem newInventory = new InventoryItemDataBuilder()
        .withId(id)
        .withCatalogItem(catalogItem)
        .withProgramId(UUID.randomUUID())
        .withFacilityId(UUID.randomUUID())
        .withStatus(FunctionalStatus.NON_FUNCTIONING)
        .withReasonNotWorkingOrNotInUse(ReasonNotWorkingOrNotInUse.NOT_APPLICABLE)
        .withUtilization(Utilization.NOT_IN_USE)
        .withVoltageStabilizer(VoltageStabilizerStatus.NO)
        .withBackupGenerator(BackupGeneratorStatus.NO)
        .withVoltageRegulator(VoltageRegulatorStatus.NO)
        .withManualTemperatureGauge(ManualTemperatureGaugeType.NO_GAUGE)
        .withRemoteTemperatureMonitor(RemoteTemperatureMonitorType.NO_RTM)
        .withEquipmentTrackingId(equipmentTrackingId)
        .withReferenceName(referenceName)
        .withYearOfInstallation(yearOfInstallation)
        .withYearOfWarrantyExpiry(withYearOfWarrantyExpiry)
        .withRemoteTemperatureMonitorId(monitorId)
        .withAdditionalNotes(additionalNotes)
        .withDecommissionDate(decommissionDate)
        .withLastModifierFirstName(firstName)
        .withLastModifierLastName(lastName)
        .withLastModifierId(lastModifierId)
        .build();

    InventoryItem expected = new InventoryItemDataBuilder()
        .withId(id)
        .withCatalogItem(catalogItemExisting)
        .withProgramId(programId)
        .withFacilityId(facilityId)
        .withStatus(FunctionalStatus.NON_FUNCTIONING)
        .withReasonNotWorkingOrNotInUse(ReasonNotWorkingOrNotInUse.NOT_APPLICABLE)
        .withUtilization(Utilization.NOT_IN_USE)
        .withVoltageStabilizer(VoltageStabilizerStatus.NO)
        .withBackupGenerator(BackupGeneratorStatus.NO)
        .withVoltageRegulator(VoltageRegulatorStatus.NO)
        .withManualTemperatureGauge(ManualTemperatureGaugeType.NO_GAUGE)
        .withRemoteTemperatureMonitor(RemoteTemperatureMonitorType.NO_RTM)
        .withEquipmentTrackingId(equipmentTrackingId)
        .withReferenceName(referenceName)
        .withYearOfInstallation(yearOfInstallation)
        .withYearOfWarrantyExpiry(withYearOfWarrantyExpiry)
        .withRemoteTemperatureMonitorId(monitorId)
        .withAdditionalNotes(additionalNotes)
        .withDecommissionDate(decommissionDate)
        .withLastModifierFirstName(firstName)
        .withLastModifierLastName(lastName)
        .withLastModifierId(lastModifierId)
        .build();

    existing.updateFrom(newInventory);

    assertEquals(expected, existing);
  }

}