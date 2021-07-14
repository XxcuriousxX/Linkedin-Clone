import { UserService } from './../user.service';
import { AuthService } from './../auth.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import {FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import { User } from '../user';
@Component({
	selector: 'app-sign-up',
	templateUrl: './sign-up.component.html',
	styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
	resp : number = 0;
	signUpForm = this.formBuilder.group({
			first_name: '',
			last_name: '',
			password: '',
			email: '',
			phone: '',
			company_name:''
			});


	constructor(private _auth: AuthService, private formBuilder: FormBuilder
										, private _usrServ: UserService) { }

	ngOnInit(): void {


	}

		onSubmit():void {
			console.log('eisai gia ton poutso intellij ', this.signUpForm.value);
			// let pipa = this._auth.registerUser(<JSON>this.signUpForm.value).subscribe(res =>  console.log(res), err => console.log(err));
			// let pipa = this._auth.registerUser(<JSON>this.signUpForm.value.email).subscribe(response => {this.resp = response.status;});
			this._usrServ.search_now()

			// console.log("This is pip:", this.res_code)
			// console.log("To res code einai:", this.response)
			// if (this.response != "SUCCESS")
			//     console.log("Yparxei idi re trompa")
			// else
			//     console.log("Perase sti basi")
			// if (pipa == null) console.log("DEN yparxei");
		}


}
