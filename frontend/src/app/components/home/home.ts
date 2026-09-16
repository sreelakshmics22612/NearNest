import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],

  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  searchText: string = '';

  locationText: string = 'Discovering businesses near you';

  sortOption: string = 'nearest';


  /* =====================================================
     CATEGORIES
     ===================================================== */

  categories = [
  {
    name: 'Home Bakery',
    count: 24,
    image: '/images/cozy kitchen.jpg'
  },
  {
    name: 'Handmade Jewellery',
    count: 18,
    image: '/images/jewellery.jpg'
  },
  {
    name: 'Candles',
    count: 15,
    image: '/images/Candles.jpg'
  },
  {
    name: 'Art & Crafts',
    count: 22,
    image: '/images/Art & Crafts.jpg'
  },
  {
    name: 'Handmade & Crochet',
    count: 17,
    image: 'images/Handmade & Crochet.jpg'
  },
  {
    name: 'Home & Décor',
    count: 19,
    image: '/images/Home & Décor.jpg'
  }
];

  /* =====================================================
     SAMPLE BUSINESS DATA
     
     IMPORTANT:
     This is temporary frontend data.
     Later these objects will come from Spring Boot.
     ===================================================== */

  businesses = [
  {
    name: "Amma's Kitchen",
    category: "Home Bakery",
    rating: 4.9,
    distance: "0.3 km",
    image: "/images/Amma's Kitchen.jpg",
    description: "Fresh homemade cakes & bakes",
    verified: true,
    open: true
  },

  {
    name: "Maya's Handmade",
    category: "Handmade Jewellery",
    rating: 4.8,
    distance: "0.5 km",
    image: "/images/Maya's Handmade.jpg",
    description: "Handcrafted jewellery made locally",
    verified: true,
    open: true
  },

  {
    name: "Glow & Grain",
    category: "Handmade Candles",
    rating: 4.7,
    distance: "0.8 km",
    image: "/images/Glow & Grain.jpg",
    description: "Hand-poured scented candles",
    verified: true,
    open: true
  },

  {
    name: "Nila Art House",
    category: "Art & Crafts",
    rating: 4.9,
    distance: "1.1 km",
    image: "/images/Nila Art House.jpg",
    description: "Original handmade art & crafts",
    verified: true,
    open: true
  },

  {
    name: "Stitch & Loop",
    category: "Crochet & Handmade",
    rating: 4.8,
    distance: "1.4 km",
    image: "/images/Stitch & Loop.jpg",
    description: "Handmade crochet gifts & decor",
    verified: true,
    open: true
  }
];

  constructor(
    private router: Router
  ) {}


  /* =====================================================
     SEARCH
     ===================================================== */

  search(): void {

    console.log(
      'Searching for:',
      this.searchText
    );

    /*
      Later:

      this.businessService
        .search(this.searchText)
        .subscribe(...)
    */

  }


  /* =====================================================
     LOCATION
     ===================================================== */

  useLocation(): void {

    if (!navigator.geolocation) {

      this.locationText =
        'Location is not supported by your browser';

      return;
    }


    this.locationText =
      'Getting your location...';


    navigator.geolocation.getCurrentPosition(

      (position) => {

        console.log(
          'Latitude:',
          position.coords.latitude
        );

        console.log(
          'Longitude:',
          position.coords.longitude
        );


        this.locationText =
          'Businesses near your location';

      },

      () => {

        this.locationText =
          'Location permission denied';

      }

    );

  }


  /* =====================================================
     CATEGORY
     ===================================================== */

  selectCategory(category: string): void {

    console.log(
      'Selected category:',
      category
    );

    this.searchText = category;

    this.search();

  }


  /* =====================================================
     BUSINESS SELECTION
     ===================================================== */

  selectBusiness(name: string): void {

    console.log(
      'Selected business:',
      name
    );

  }


  /* =====================================================
     OPEN BUSINESS
     ===================================================== */

  openBusiness(business: any): void {

    console.log(
      'Opening business:',
      business
    );

    /*
      Later, after backend business API:

      this.router.navigate([
        '/business',
        business.id
      ]);

    */

  }

}