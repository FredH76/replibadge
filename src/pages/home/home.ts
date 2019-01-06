import { Component } from '@angular/core';
import { NavController, ToastController } from 'ionic-angular';
import chameleon from '../../../plugins/cordova-plugin-chameleon/www/chameleon';


@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  public badgeArray: Uint8Array;

  constructor(
    public navCtrl: NavController,
    private toastCtrl: ToastController,
  ) {

  }

  /**************************************************************************************
   *  Read Badge from Chameleon Device
   *************************************************************************************/
  readBadge() {

  }

  /**************************************************************************************
   *  Upload Badge to Chameleon Device
   *************************************************************************************/
  uploadBadge() {
    this.transferBadgeToCham();
  }

  /**************************************************************************************
   * TRANSFER BADGE from APP to CHAMELEON
   *************************************************************************************/
  transferBadgeToCham() {
    // initialize USB driver
    chameleon.initialize(
      null,
      success => this.isPresent(),
      error => this.presentToastError("Le driver n'a pas pu être initialisé. Transfert annulé.")
    )
  }

  // test if usb is connected
  private isPresent() {
    chameleon.isPresent(
      null,
      success => this.sendBadge(),
      error => {
        this.presentToastError("Le Chameleon n'est pas connecté. Transfert annulé.");
        this.closeUSB();
      }
    )
  };

  // send badge (préviously dowloaded and stored in this.badgeArray)
  private sendBadge() {
    chameleon.uploadArray(
      this.badgeArray,
      success => {
        this.presentToastSuccess("Chargement du badge réussi");
        this.closeUSB()
      },
      error => {
        this.presentToastError("Le chargement n'a pu être effectué. Transfert annulé.");
        this.closeUSB();
      }
    )
  };

  // close USB
  private closeUSB() {
    chameleon.shutdown();
  }


  /**************************************************************************************
   * IHM functions for LOADING and MESSAGE display
   *************************************************************************************/
  presentToastSuccess(msg?: string) {
    let toast = this.toastCtrl.create({
      message: msg || "Ok",
      position: "top",
      cssClass: "toast valid",
      duration: 2000
    });
    toast.present();
  }

  presentToastError(error?: string) {
    let toast = this.toastCtrl.create({
      message: error || "Ooops, une erreur est survenue.",
      position: "top",
      showCloseButton: true,
      cssClass: "toast error",
      closeButtonText: "Fermer"
    });
    toast.present();
  }

}
