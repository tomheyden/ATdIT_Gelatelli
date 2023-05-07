# Operating Manual

## 1 Introduction
This manual will provide you with a comprehensive guide on how to use the Eiscafe Gelatelli software to manage your ice cream business effectively.

## 2 Installation
Please refer to the [Installation Guide](https://github.com/tomheyden/ATdIT_Gelatelli/blob/ReadMe/Installation%20Guide.md) for information on how to install the software.
## 3 Getting Started
After installation, launch the Eiscafe Gelatelli application to begin using the software.
## 4 Using the Software
Welcome to our Eiscafe-Gelatelli-IT-System! The Following user-guide will show you the functionalities of our Application and how to use it.
### 4.1 Home Screen
![Homescreen Eiscafe_Gelatelli IT-System German](https://user-images.githubusercontent.com/128143410/236630401-72f2e5fa-81fc-4b4e-b218-0d33925720db.png)

*Picture 1: Eiscafe-Gelatelli IT-System home screen in German*

The home screen (Picture 1) displays three buttons:

- Language Button: Located in the upper left corner of the frame, the "Language" button can be used to change the language of the application (see Picture 2).
- Production Button: This button will redirect you to the production environment of our application.
- Warehouse Button: This button will redirect you to the warehouse site of our application.

You can also see that the application has established a connection to the database, indicated by the green "Database connected" slogan above the Production and Warehouse buttons.

![Language_Switch_Homescreen_en](https://user-images.githubusercontent.com/128143410/236630147-3a514b59-6e62-40c5-9063-d844bfd769dd.png)

*Picture 2: Changing the language of the application using the Language button*

### 4.2 Production Environment

![Production Environment in the Eiscafe Gelatelli IT-System](https://user-images.githubusercontent.com/128143410/236634464-e7567d36-9e2a-4378-b727-ae8c346b924c.png)

*Picture 3: Production environment in the Eiscafe Gelatelli IT-System*

The production environment allows users to select a flavor and a quantity of batches to begin the production process automatically. Additionally, users can view the recipe for a certain flavor and the current inventory of each ingredient and their expiration dates. 

### Step-by-Step Guide for Production

#### 1. **Choose a Flavor**

   ![Selection of producible ice cream flavors](https://user-images.githubusercontent.com/128143410/236634850-c6a22755-1cc3-4304-8164-b981a0b3afca.png)

   *Picture 4: Selection of producible ice cream flavors*

   After clicking on "Choose Flavor," a dropdown menu will appear allowing you to select the ice cream flavor you would like to produce.

#### 2. **Show Recipe**

   ![Recipe displayed for selected ice-cream flavour](https://user-images.githubusercontent.com/128143410/236635151-1558c17f-ea00-40c3-b879-a7ed6f62ad4a.png)

   *Picture 5: Recipe displayed for selected ice-cream flavour*

   After selecting your flavor of choice, click the "Show recipe" button to display the recipe for one batch of ice cream in the recipe field in the upper right quadrant of the application frame.

#### 3. **Choose Amount**

   ![Choose the amount of batches of ice cream you'd like to produce](https://user-images.githubusercontent.com/128143410/236634967-d2dd35dd-acd2-4772-82ea-794f718068de.png)

   *Picture 6: Choose the amount of batches of ice cream you'd like to produce (producible amounts range from 1-20)*

   Choose the number of batches you'd like to produce by selecting a quantity between 1 and 20 from the dropdown menu below "Choose amount."

#### 4. **Produce**

   ![Production progress of ice cream](https://user-images.githubusercontent.com/128143410/236635359-930f2c83-fc19-4eb6-81d6-81941a63e15e.png)

   *Picture 7: Production progress of ice cream*

   After completing the previous steps, start the production process by clicking the "Produce" button. The progress bar below the button will fill slowly in blue from left to right. If the production process is successful, the progress bar will turn green. If there are any issues, such as missing ingredients, the program will display an error message and the progress bar will turn red.

   ![Failed production process due to missing ingredient](https://user-images.githubusercontent.com/128143410/236635520-2c975478-3bcc-4522-84f8-d3aadeed40db.png)

   *Picture 8: Failed production process due to missing ingredient*

### 4.3 Warehouse Environment

![Warehouse Environment](https://user-images.githubusercontent.com/128143410/236636373-67908a4e-97e5-410e-aef3-03cf95ca9719.png)
*Picture 9: Warehouse environment*

The left side of the frame shows the contents of your warehouse. Each content is displayed: Amount, Unit, Ingredient name, best before date. Above the window displaying the contents you have the option to choose a filter to filter your warehouse after different criteria. The following step-by-step guide will provide you with the information necessary to add new goods to your warehouse.

### Step-by-Step-Guide for the Warehouse

#### 1. Choose an Ingredient
![Select Incoming Good](https://user-images.githubusercontent.com/128143410/236636673-9d53996c-f11a-4df6-89ff-2dcd3ed2bee5.png)
*Picture 10: Selection of Possible Incoming Goods*

To start the process of cataloging new incoming goods in your warehouse you have to first choose what ingredient is arriving in the incoming shipment. To do so, please click on the dropdown menu which is labeled with "Choose Ingredient". After clicking on the dropdown menu you will be provided with a selection of all the ingredients that you need to produce the flavors available in your ice cream shop. Choose the designated incoming ingredient.

#### 2. Select the Amount
![Select Amount Incoming Good](https://user-images.githubusercontent.com/128143410/236636908-8f072153-0f50-4ca3-b9de-1997c83df56b.png)
*Picture 11: Selection of the possible amounts for an incoming good*

After choosing which ingredient is incoming you can now choose the amount of the ingredient incoming. The incoming amount for one shipment of a certain ingredient can reach from 1 to 20 units.

#### 3. Select the Unit
![Select Unit Incoming Good](https://user-images.githubusercontent.com/128143410/236637035-2be09d5b-3987-498f-a002-60aadce265cf.png)
*Picture 12: Possible units for incoming goods*

After selecting the amount of the incoming good you need to select the unit of the arriving good. To select a unit please click on the dropdown menu under the "Unit" label and choose between "l" for liters or "kg" for kilograms.

#### 4. Select the Best Before Date
![Set BBD Incoming Good](https://user-images.githubusercontent.com/128143410/236637293-3de7eb40-c578-4922-bc5c-bf9a3f2afac2.png)
*Picture 13: Select the BBD*

After completing the other steps you need to select the best before date. To select the best before date, please click on the calendar icon. A calendar will show up where you can choose the best before date for your incoming ingredient.



