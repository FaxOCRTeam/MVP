package services.dao.Fax2EMR.model;


public class Fax2EMRPatientInformation {
	public Integer id;
	public String Last_Name;
	public String First_Name;
	public String Date_Of_Birth;
	public String Address;
	public String Phone;
	public String Medical_Record_Number;
	public String Collection_Date;
	public String Collection_Time;
	public String Diagnosis;
	public String Client_code;
	public Integer getId() {
		return this.id;
	}
	public String getLastName() {
		return this.Last_Name;
	}
	public String getFirstName() {
		return this.First_Name;
	}
	public String getDateOfBirth() {
		return this.Date_Of_Birth;
	}
	public String getAddress() {
		return this.Address;
	}
	public String getPhone() {
		return this.Phone;
	}
	public String getMedicalRecordNumber() {
		return this.Medical_Record_Number;
	}
	public String getCollectionDate() {
		return this.Collection_Date;
	}
	public String getCollectionTime() {
		return this.Collection_Time;
	}
	public String getDiagnosis() {
		return this.Diagnosis;
	}
	public String getClientCode() {
		return this.Client_code;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setLastName(String Last_Name) {
		this.Last_Name = Last_Name;
	}
	public void setFirstName(String First_Name) {
		this.First_Name = First_Name;
	}
	public void setDateOfBirth(String Date_Of_Birth) {
		this.Date_Of_Birth = Date_Of_Birth;
	}
	public void setAddress(String Address) {
		this.Address = Address;
	}
	public void setPhone(String Phone) {
		this.Phone = Phone;
	}
	public void setMedicalRecordNumber(String Medical_Record_Number) {
		this.Medical_Record_Number = Medical_Record_Number;
	}
	public void setCollectionDate(String Collection_Date) {
		this.Collection_Date = Collection_Date;
	}
	public void setCollectionTime(String Collection_Time) {
		this.Collection_Time = Collection_Time;
	}
	public void setDiagnosis(String Diagnosis) {
		this.Diagnosis = Diagnosis;
	}
	public void setClientCode(String Client_code) {
		this.Client_code = Client_code;
	}
}
