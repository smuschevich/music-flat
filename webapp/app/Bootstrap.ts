import {bootstrap} from 'angular2/platform/browser';
import {ROUTER_PROVIDERS, LocationStrategy, HashLocationStrategy} from 'angular2/router';
import {AppComponent} from './AppComponent';
import {provide} from 'angular2/core';
import {HTTP_PROVIDERS} from 'angular2/http';
import {SecurityRemoteService} from './common/remote/SecurityRemoteService';
import {SecurityService} from './common/service/SecurityService';

bootstrap(AppComponent, [
	ROUTER_PROVIDERS,
	provide(LocationStrategy, {useClass: HashLocationStrategy}),
	HTTP_PROVIDERS,
	SecurityRemoteService,
	SecurityService
]);