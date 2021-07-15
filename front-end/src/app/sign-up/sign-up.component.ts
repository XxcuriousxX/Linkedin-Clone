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
	message: any;
	response: any;
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

	async onSubmit() {
		console.log('eisai gia ton poutso intellij ', this.signUpForm.value);

		// this._usrServ.search_now(this.signUpForm.value.email)
		// let s = this._usrServ.doRegistration(this.signUpForm.value.email).subscribe((data)=>this.message=data);
		await this._usrServ.getValFromObs(this.signUpForm.value.email).then((res:any) => this.response = "SUCCESS",
		(err:any) => this.response = "FAILURE");
		
		console.log("This is it: ", this.response);
	}


}
