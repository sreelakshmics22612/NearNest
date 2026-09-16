import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-business-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './business-dashboard.html',
  styleUrl: './business-dashboard.css'
})
export class BusinessDashboard implements OnInit, OnDestroy {

  // =========================================================
  // PAGE STATE
  // =========================================================
  isSetupMode = true;
  isSaving = false;
  activeSection: string = 'overview';

business: any = null;

calendarDate: Date = new Date();

calendarDays: {
  date: Date;
  dayNumber: number;
  isCurrentMonth: boolean;
  isToday: boolean;
  isUnavailable: boolean;
  reason: string | null;
}[] = [];

availabilityRecords: any[] = [];

selectedCalendarDate: Date | null = null;

calendarLoading = false;
calendarSaving = false;

calendarMessage = '';
unavailableReason = '';

  // Complete business object returned by Spring Boot.
  // businessId is required for business-specific API calls.


  setActiveSection(section: string): void {
    this.activeSection = section;

    if (section === 'calendar' && this.business?.businessId) {
      this.loadCalendar();
    }
  }

  

  private apiUrl = 'http://localhost:8080/api/businesses';


  // =========================================================
  // ROTATING VISUAL IMAGES
  // =========================================================
  visualImages = [
    {
      src: 'images/Kerala woman.jpg',
      title: 'Made close to home'
    },
    {
      src: 'images/Homemade Food.jpg',
      title: 'Homegrown goodness'
    },
    {
      src: 'images/jewellery.jpg',
      title: 'Handmade with care'
    },
    {
      src: 'images/Nila Art House.jpg',
      title: 'Made by local hands'
    }
  ];

  currentImageIndex = 0;

  private imageInterval: ReturnType<typeof setInterval> | null = null;


  // =========================================================
  // CONSTRUCTOR
  // =========================================================

  constructor(
  private http: HttpClient,
  private cdr: ChangeDetectorRef
) {}


  // =========================================================
  // START IMAGE SLIDER
  // =========================================================

 ngOnInit(): void {

  // Check whether this business owner already has a profile
  this.checkExistingBusiness();

  this.imageInterval = setInterval(() => {

    this.currentImageIndex =
      (this.currentImageIndex + 1) %
      this.visualImages.length;

  }, 4500);

}
checkExistingBusiness(): void {

  console.log(' CHECKING EXISTING BUSINESS');

  const userId = localStorage.getItem('userId');

  console.log(' STORED USER ID:', userId);

  if (!userId) {
    console.error('No userId found in localStorage');
    this.isSetupMode = true;
    return;
  }

  this.http
    .get<any>(`${this.apiUrl}/user/${userId}`)
    .subscribe({

      next: (business) => {
        console.log(' BUSINESS RESPONSE:', business);

        // Preserve the complete backend response, especially businessId.
        this.business = business;

        console.log(
          'BUSINESS FOUND — ID:',
          this.business?.businessId
        );

        console.log(' BUSINESS FOUND — SWITCHING TO DASHBOARD');

        this.businessName = business.businessName || '';
        this.contactNumber = business.contactNumber || '';
        this.location = business.location || '';

        if (business.category) {
          this.category = business.category.name || '';
        }

        this.latitude = business.latitude ?? null;
        this.longitude = business.longitude ?? null;

       this.isSetupMode = false;

      console.log('isSetupMode =', this.isSetupMode);

      this.cdr.detectChanges();

      console.log('Dashboard change detection triggered');
        },

      error: (error) => {

        console.error(' BUSINESS CHECK ERROR:', error);
        console.log('ERROR STATUS:', error.status);

        if (error.status === 404) {
          this.isSetupMode = true;
        } else {
          this.isSetupMode = true;
        }

      }

    });
}

  // =========================================================
  // STOP IMAGE SLIDER
  // =========================================================

  ngOnDestroy(): void {

    if (this.imageInterval !== null) {

      clearInterval(this.imageInterval);

      this.imageInterval = null;
    }

  }


  // =========================================================
  // BUSINESS DATA
  // =========================================================

  businessName = '';
  category = '';
  contactNumber = '';
  location = '';

  latitude: number | null = null;
  longitude: number | null = null;


  // =========================================================
  // FILES
  // =========================================================

  logoFile: File | null = null;
  identityFile: File | null = null;

  logoPreview: string | null = null;

  workPhotos: File[] = [];
  workPhotoPreviews: string[] = [];


  // =========================================================
  // BUSINESS CATEGORIES
  // =========================================================

  categories = [
    'Food & Home Kitchen',
    'Handmade & Crafts',
    'Fashion & Clothing',
    'Beauty & Personal Care',
    'Home Services',
    'Repair & Maintenance',
    'Tutoring & Education',
    'Health & Wellness',
    'Local Retail',
    'Other'
  ];


  // =========================================================
  // CATEGORY ID
  // =========================================================

  getCategoryId(): number | null {

    const index = this.categories.indexOf(this.category);

    if (index === -1) {
      return null;
    }

    /*
     * Categories were initialized in this order:
     *
     * 1  Food & Home Kitchen
     * 2  Handmade & Crafts
     * 3  Fashion & Clothing
     * 4  Beauty & Personal Care
     * 5  Home Services
     * 6  Repair & Maintenance
     * 7  Tutoring & Education
     * 8  Health & Wellness
     * 9  Local Retail
     * 10 Other
     *
     * Therefore the database ID is index + 1.
     */

    return index + 1;
  }


  // =========================================================
  // LOGO UPLOAD
  // =========================================================

  onLogoSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    if (!file.type.startsWith('image/')) {

      alert('Please select an image file.');

      return;
    }

    this.logoFile = file;

    if (this.logoPreview) {

      URL.revokeObjectURL(this.logoPreview);
    }

    this.logoPreview = URL.createObjectURL(file);
  }


  // =========================================================
  // IDENTITY DOCUMENT
  // =========================================================

  onIdentitySelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    const allowedTypes = [
      'image/jpeg',
      'image/png',
      'application/pdf'
    ];

    if (!allowedTypes.includes(file.type)) {

      alert('Please upload JPG, PNG or PDF.');

      return;
    }

    this.identityFile = file;
  }


  // =========================================================
  // WORK PHOTOS
  // =========================================================

  onWorkPhotosSelected(event: Event): void {

    const input = event.target as HTMLInputElement;

    if (!input.files) {
      return;
    }

    const files = Array.from(input.files);

    const imageFiles = files.filter(file =>
      file.type.startsWith('image/')
    );

    if (imageFiles.length === 0) {

      alert('Please select image files.');

      return;
    }

    const remainingSlots =
      6 - this.workPhotos.length;

    const filesToAdd =
      imageFiles.slice(0, remainingSlots);

    this.workPhotos.push(...filesToAdd);

    filesToAdd.forEach(file => {

      this.workPhotoPreviews.push(
        URL.createObjectURL(file)
      );

    });

    if (imageFiles.length > remainingSlots) {

      alert('You can upload up to 6 work photos.');
    }

    input.value = '';
  }


  // =========================================================
  // REMOVE WORK PHOTO
  // =========================================================

  removeWorkPhoto(index: number): void {

    if (
      index < 0 ||
      index >= this.workPhotos.length
    ) {

      return;
    }

    const preview =
      this.workPhotoPreviews[index];

    if (preview) {

      URL.revokeObjectURL(preview);
    }

    this.workPhotos.splice(index, 1);

    this.workPhotoPreviews.splice(index, 1);
  }


  // =========================================================
  // REMOVE LOGO
  // =========================================================

  removeLogo(): void {

    if (this.logoPreview) {

      URL.revokeObjectURL(this.logoPreview);
    }

    this.logoFile = null;
    this.logoPreview = null;
  }


  // =========================================================
  // CURRENT LOCATION
  // =========================================================

  useCurrentLocation(): void {

    if (!navigator.geolocation) {

      alert(
        'Location is not supported by your browser.'
      );

      return;
    }

    navigator.geolocation.getCurrentPosition(

      position => {

        this.latitude =
          position.coords.latitude;

        this.longitude =
          position.coords.longitude;

        this.location =
          `Current location (${this.latitude.toFixed(5)}, ${this.longitude.toFixed(5)})`;

      },

      error => {

        console.error(
          'Location error:',
          error
        );

        alert(
          'Unable to get your location. Please allow location permission.'
        );

      },

      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0
      }

    );
  }


  // =========================================================
  // FORM VALIDATION
  // =========================================================

  isFormValid(): boolean {

    return (

      this.businessName.trim().length >= 2 &&

      this.category.trim().length > 0 &&

      this.contactNumber.trim().length >= 10 &&

      this.location.trim().length > 0 &&

      this.logoFile !== null &&

      this.identityFile !== null

    );

  }


  // =========================================================
  // SAVE BUSINESS
  // =========================================================

  saveBusiness(): void {

    // -------------------------------------------------------
    // VALIDATE FORM
    // -------------------------------------------------------

    if (!this.isFormValid()) {

      alert(
        'Please complete all required business details.'
      );

      return;
    }


    // -------------------------------------------------------
    // GET LOGGED-IN USER ID
    // -------------------------------------------------------

    const storedUserId =
      localStorage.getItem('userId');

    if (!storedUserId) {

      alert(
        'Your login session is missing. Please log in again.'
      );

      return;
    }

    const userId =
      Number(storedUserId);

    if (!Number.isInteger(userId) || userId <= 0) {

      alert(
        'Invalid user session. Please log in again.'
      );

      return;
    }


    // -------------------------------------------------------
    // GET CATEGORY ID
    // -------------------------------------------------------

    const categoryId =
      this.getCategoryId();

    if (!categoryId) {

      alert(
        'Please select a valid business category.'
      );

      return;
    }


    // -------------------------------------------------------
    // START SAVING
    // -------------------------------------------------------

    this.isSaving = true;


    // -------------------------------------------------------
    // CREATE MULTIPART FORM DATA
    // -------------------------------------------------------

    const formData = new FormData();

    formData.append(
      'userId',
      userId.toString()
    );

    formData.append(
      'businessName',
      this.businessName.trim()
    );

    formData.append(
      'categoryId',
      categoryId.toString()
    );

    formData.append(
      'contactNumber',
      this.contactNumber.trim()
    );

    formData.append(
      'location',
      this.location.trim()
    );


    // -------------------------------------------------------
    // LATITUDE / LONGITUDE
    // -------------------------------------------------------

    if (this.latitude !== null) {

      formData.append(
        'latitude',
        this.latitude.toString()
      );
    }

    if (this.longitude !== null) {

      formData.append(
        'longitude',
        this.longitude.toString()
      );
    }


    // -------------------------------------------------------
    // LOGO
    // -------------------------------------------------------

    if (this.logoFile) {

      formData.append(
        'logo',
        this.logoFile
      );
    }


    // -------------------------------------------------------
    // IDENTITY DOCUMENT
    // -------------------------------------------------------

    if (this.identityFile) {

      formData.append(
        'identityDocument',
        this.identityFile
      );
    }


    // -------------------------------------------------------
    // SEND TO SPRING BOOT
    // -------------------------------------------------------

    this.http.post(
      this.apiUrl,
      formData
    ).subscribe({

      // =====================================================
      // SUCCESS
      // =====================================================

      next: (response) => {

        console.log(
          'BUSINESS CREATED SUCCESSFULLY:',
          response
        );

        this.business = response;

        this.isSaving = false;

        /*
         * Change from setup page
         * to business dashboard.
         */
        this.isSetupMode = false;

        alert(
          'Business profile created successfully!'
        );

      },


      // =====================================================
      // ERROR
      // =====================================================

      error: (error) => {

        console.error(
          'BUSINESS CREATION FAILED:',
          error
        );

        this.isSaving = false;

        if (error.error) {

          console.error(
            'BACKEND ERROR:',
            error.error
          );
        }

        alert(
          'Unable to create your business. Please try again.'
        );

      }

    });

  }


  // =========================================================
  // EDIT PROFILE
  // =========================================================

  editProfile(): void {

    this.isSetupMode = true;

  }


  // =========================================================
  // BUSINESS AVAILABILITY CALENDAR
  // =========================================================
  //
  // Matches the current MySQL table:
  // business_availability
  //   availability_id
  //   business_id
  //   unavailable_date
  //   reason
  //   created_at
  //
  // No dashboard business data is hardcoded here.
  // =========================================================

  // =========================================================
  // CALENDAR NAVIGATION
  // =========================================================

  previousMonth(): void {
    this.calendarDate = new Date(
      this.calendarDate.getFullYear(),
      this.calendarDate.getMonth() - 1,
      1
    );

    this.generateCalendar();
  }

  nextMonth(): void {
    this.calendarDate = new Date(
      this.calendarDate.getFullYear(),
      this.calendarDate.getMonth() + 1,
      1
    );

    this.generateCalendar();
  }

  goToToday(): void {
    this.calendarDate = new Date();
    this.generateCalendar();
  }


  // =========================================================
  // LOAD CALENDAR FROM BACKEND
  // =========================================================

  loadCalendar(): void {
    const businessId = this.getBusinessId();

    if (!businessId) {
      console.error('Business ID not found.');
      return;
    }

    this.calendarLoading = true;
    this.calendarMessage = '';

    this.http.get<any[]>(
      `${this.apiUrl}/${businessId}/availability`
    ).subscribe({
      next: (records) => {
        this.availabilityRecords =
          Array.isArray(records) ? records : [];

        this.generateCalendar();
        this.calendarLoading = false;
      },

      error: (error) => {
        console.error(
          'Failed to load business availability:',
          error
        );

        this.availabilityRecords = [];
        this.generateCalendar();
        this.calendarLoading = false;

        this.calendarMessage =
          'Unable to load availability data.';
      }
    });
  }


  // =========================================================
  // GENERATE CALENDAR
  // =========================================================

  generateCalendar(): void {
    const year = this.calendarDate.getFullYear();
    const month = this.calendarDate.getMonth();

    const firstDay = new Date(year, month, 1);

    // Monday = 0 ... Sunday = 6
    const firstWeekday =
      (firstDay.getDay() + 6) % 7;

    const daysInMonth =
      new Date(year, month + 1, 0).getDate();

    const previousMonthDays =
      new Date(year, month, 0).getDate();

    const today = new Date();

    this.calendarDays = [];

    // Six rows × seven days.
    for (let i = 0; i < 42; i++) {

      const dayOffset =
        i - firstWeekday + 1;

      let date: Date;
      let isCurrentMonth = true;

      if (dayOffset <= 0) {

        date = new Date(
          year,
          month - 1,
          previousMonthDays + dayOffset
        );

        isCurrentMonth = false;

      } else if (dayOffset > daysInMonth) {

        date = new Date(
          year,
          month,
          dayOffset
        );

        isCurrentMonth = false;

      } else {

        date = new Date(
          year,
          month,
          dayOffset
        );
      }

      this.calendarDays.push({
        date,
        dayNumber: date.getDate(),
        isCurrentMonth,
        isToday: this.isSameDate(date, today),
        isUnavailable: this.isDateUnavailable(date),
        reason: this.getUnavailableReason(date)
      });
    }
  }


  // =========================================================
  // DATE HELPERS
  // =========================================================

  isSameDate(
    first: Date,
    second: Date
  ): boolean {

    return (
      first.getFullYear() === second.getFullYear() &&
      first.getMonth() === second.getMonth() &&
      first.getDate() === second.getDate()
    );
  }


  private formatDate(date: Date): string {

    const year =
      date.getFullYear();

    const month =
      String(date.getMonth() + 1)
        .padStart(2, '0');

    const day =
      String(date.getDate())
        .padStart(2, '0');

    return `${year}-${month}-${day}`;
  }


  isDateUnavailable(date: Date): boolean {

  const target = this.formatDate(date);

  return this.availabilityRecords.some(record => {

    const savedDate =
      record?.unavailableDate
        ? String(record.unavailableDate).substring(0, 10)
        : '';

    return savedDate === target;
  });
}


 private getUnavailableReason(
  date: Date
): string | null {

  const target = this.formatDate(date);

  const record =
    this.availabilityRecords.find(item => {

      const savedDate =
        item?.unavailableDate
          ? String(item.unavailableDate).substring(0, 10)
          : '';

      return savedDate === target;
    });

  return record?.reason ?? null;
}


  // =========================================================
  // SELECT DATE
  // =========================================================

  selectCalendarDate(day: {
    date: Date;
    dayNumber: number;
    isCurrentMonth: boolean;
    isToday: boolean;
    isUnavailable: boolean;
    reason: string | null;
  }): void {

    // Clicking an adjacent-month day moves the calendar
    // to that month before selecting it.
    if (!day.isCurrentMonth) {

      this.calendarDate = new Date(
        day.date.getFullYear(),
        day.date.getMonth(),
        1
      );

      this.generateCalendar();
    }

    this.selectedCalendarDate =
      new Date(day.date);

    this.unavailableReason =
      day.reason || '';

    this.calendarMessage = '';
  }


  // =========================================================
  // MARK SELECTED DATE UNAVAILABLE
  // =========================================================

 markSelectedDateUnavailable(): void {

  const businessId = this.getBusinessId();

  if (!businessId) {
    this.calendarMessage = 'Business information is not available.';
    return;
  }

  if (!this.selectedCalendarDate) {
    this.calendarMessage = 'Please select a date first.';
    return;
  }

  const date = this.formatDate(this.selectedCalendarDate);
  const reason = this.unavailableReason.trim();

  const params: any = {
    date: date
  };

  if (reason) {
    params.reason = reason;
  }

  this.calendarSaving = true;
  this.calendarMessage = '';

  console.log('================================');
  console.log('SAVING AVAILABILITY');
  console.log('Business ID:', businessId);
  console.log('Date:', date);
  console.log('Reason:', reason);
  console.log('================================');

  this.http.post<any>(
    `${this.apiUrl}/${businessId}/availability`,
    null,
    { params }
  ).subscribe({

    next: (record) => {

      console.log('DATABASE SAVE SUCCESS');
      console.log('Returned record:', record);

      /*
       * IMPORTANT:
       * Only update the calendar after
       * Spring Boot confirms the database save.
       */

      const index =
        this.availabilityRecords.findIndex(
          item => item.unavailableDate === date
        );

      if (index >= 0) {

        this.availabilityRecords[index] = record;

      } else {

        this.availabilityRecords.push(record);

      }

      this.generateCalendar();

      this.calendarSaving = false;

      this.calendarMessage =
        'Updated — date marked unavailable.';

    },

    error: (error) => {

      console.error('================================');
      console.error('DATABASE SAVE FAILED');
      console.error('HTTP STATUS:', error.status);
      console.error('ERROR:', error);
      console.error('BACKEND RESPONSE:', error.error);
      console.error('================================');

      this.calendarSaving = false;

      this.calendarMessage =
        'Could not save the date to the database.';
    }

  });

}

  // =========================================================
  // MAKE SELECTED DATE AVAILABLE AGAIN
  // =========================================================


  // =========================================================
  // GET BUSINESS ID
  // =========================================================

  getBusinessId(): number | null {

    if (!this.business?.businessId) {
      return null;
    }

    const businessId =
      Number(this.business.businessId);

    if (
      !Number.isInteger(businessId) ||
      businessId <= 0
    ) {
      return null;
    }

    return businessId;
  }

makeSelectedDateAvailable(): void {

  const businessId = this.getBusinessId();

  if (!businessId) {
    this.calendarMessage =
      'Business information is not available.';
    return;
  }

  if (!this.selectedCalendarDate) {
    this.calendarMessage =
      'Please select a date first.';
    return;
  }

  const date =
    this.formatDate(this.selectedCalendarDate);

  if (!this.isDateUnavailable(this.selectedCalendarDate)) {
    this.calendarMessage =
      'The selected date is already available.';
    return;
  }

  this.calendarSaving = true;
  this.calendarMessage = '';

  this.http.delete(
    `${this.apiUrl}/${businessId}/availability/${date}`
  ).subscribe({

    next: (records) => {

  console.log('==============================');
  console.log('CALENDAR BUSINESS ID:', businessId);
  console.log('DATABASE AVAILABILITY:', records);
  console.log('==============================');

  this.availabilityRecords =
    Array.isArray(records) ? records : [];

  this.generateCalendar();

  this.calendarLoading = false;

  this.cdr.detectChanges();
},

    error: (error) => {

      console.error(
        'FAILED TO REMOVE UNAVAILABLE DATE:',
        error
      );

      this.calendarSaving = false;

      this.calendarMessage =
        'Could not update the database.';
    }

  });
}

}
