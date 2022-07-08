package com.uk.myhss.AddMember.PincodeAddress

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Get_PincodeAddress {
    @SerializedName("AddressId")
    @Expose
    var addressId: Int? = null

    @SerializedName("AdministrativeCounty")
    @Expose
    var administrativeCounty: String? = null

    @SerializedName("Barcode")
    @Expose
    var barcode: String? = null

    @SerializedName("BuildingName")
    @Expose
    var buildingName: String? = null

    @SerializedName("BuildingNumber")
    @Expose
    var buildingNumber: String? = null

    @SerializedName("Company")
    @Expose
    var company: String? = null

    @SerializedName("CountryName")
    @Expose
    var countryName: String? = null

    @SerializedName("County")
    @Expose
    var county: String? = null

    @SerializedName("DeliveryPointSuffix")
    @Expose
    var deliveryPointSuffix: String? = null

    @SerializedName("Department")
    @Expose
    var department: String? = null

    @SerializedName("DependentLocality")
    @Expose
    var dependentLocality: String? = null

    @SerializedName("DoubleDependentLocality")
    @Expose
    var doubleDependentLocality: String? = null

    @SerializedName("Easting")
    @Expose
    var easting: Int? = null

    @SerializedName("Latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("LatitudeShort")
    @Expose
    var latitudeShort: Double? = null

    @SerializedName("Line1")
    @Expose
    var line1: String? = null

    @SerializedName("Line2")
    @Expose
    var line2: String? = null

    @SerializedName("Line3")
    @Expose
    var line3: String? = null

    @SerializedName("Line4")
    @Expose
    var line4: String? = null

    @SerializedName("Longitude")
    @Expose
    var longitude: Double? = null

    @SerializedName("LongitudeShort")
    @Expose
    var longitudeShort: Double? = null

    @SerializedName("Northing")
    @Expose
    var northing: Int? = null

    @SerializedName("Pobox")
    @Expose
    var pobox: String? = null

    @SerializedName("PostTown")
    @Expose
    var postTown: String? = null

    @SerializedName("PostalCounty")
    @Expose
    var postalCounty: String? = null

    @SerializedName("Postcode")
    @Expose
    var postcode: String? = null

    @SerializedName("PrimaryStreet")
    @Expose
    var primaryStreet: String? = null

    @SerializedName("PrimaryStreetName")
    @Expose
    var primaryStreetName: String? = null

    @SerializedName("PrimaryStreetType")
    @Expose
    var primaryStreetType: String? = null

    @SerializedName("SecondaryStreet")
    @Expose
    var secondaryStreet: String? = null

    @SerializedName("SecondaryStreetName")
    @Expose
    var secondaryStreetName: String? = null

    @SerializedName("SecondaryStreetType")
    @Expose
    var secondaryStreetType: String? = null

    @SerializedName("StreetAddress1")
    @Expose
    var streetAddress1: String? = null

    @SerializedName("StreetAddress2")
    @Expose
    var streetAddress2: Any? = null

    @SerializedName("StreetAddress3")
    @Expose
    var streetAddress3: Any? = null

    @SerializedName("SubBuilding")
    @Expose
    var subBuilding: String? = null

    @SerializedName("TraditionalCounty")
    @Expose
    var traditionalCounty: String? = null

    @SerializedName("Type")
    @Expose
    var type: String? = null
}