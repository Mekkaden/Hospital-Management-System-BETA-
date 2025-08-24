package hms;
public  class Receptionist {

    public void registerPatient() 
       { /*  Registers a new patient into
         the hospital system by collecting 
       personal and medical details */}   

    public void bookAppointment() 
      { /*  Books an appointment for a 
        patient with a specific doctor
      at a chosen time slot*/}
    
     public void manageAppointments() 
      {/*Allows updating, rescheduling, 
      or canceling of patient appointments*/}
    
    public void viewDoctorSchedule()
       { /* Displays the schedule of 
        a doctor, including all booked
       and available slots.*/ }
    
    public void generateToken() 
      {  /*Generates a unique token or 
        queue number for a patient's visit
        to streamline check-ins*/ }

    public void transferToDoctorQueue() 
       { /*  Moves a patient from the waiting
         area to the doctor's queue when 
         their token is called*/}   
}

    

