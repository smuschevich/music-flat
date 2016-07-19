import {bootstrap} from '@angular/platform-browser-dynamic';
import {LocationStrategy, HashLocationStrategy} from '@angular/common';
import {ROUTER_PROVIDERS} from '@angular/router-deprecated';
import {AppComponent} from './AppComponent';
import {provide} from '@angular/core';
import {HTTP_PROVIDERS} from '@angular/http';
import {SecurityRemoteService} from './common/remote/SecurityRemoteService';
import {SecurityService} from './common/service/SecurityService';

bootstrap(AppComponent, [
	ROUTER_PROVIDERS,
	provide(LocationStrategy, {useClass: HashLocationStrategy}),
	HTTP_PROVIDERS,
	SecurityRemoteService,
	SecurityService
]);