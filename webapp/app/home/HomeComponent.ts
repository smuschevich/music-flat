import {Component} from 'angular2/core';
import {LoginComponent} from './LoginComponent';

@Component({
	selector: 'home',
	templateUrl: 'app/home/Home.html',
	styleUrls: ['app/home/Home.css'],
	directives: [
		LoginComponent
	]
})
export class HomeComponent {
}