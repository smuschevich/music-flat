import {Directive, ViewContainerRef, DynamicComponentLoader} from '@angular/core';
import {RouterOutlet, Router, ComponentInstruction} from '@angular/router-deprecated';
import {SecurityService} from './common/service/SecurityService';

@Directive({
	selector: 'router-outlet'
})
export class LoggedInRouterOutlet extends RouterOutlet {
	private static PUBLIC_ROUTES = {
		'home': true
	};
	private static HOME_ROUTE = 'home';
	private static AUTHORIZED_ROUTE = 'dashboard';

	constructor(
		_viewContainerRef: ViewContainerRef,
		_loader: DynamicComponentLoader,
		private parentRouter: Router,
		nameAttr: string,
		private securityService: SecurityService) {

		super(_viewContainerRef, _loader, parentRouter, nameAttr);
	}

	activate(nextInstruction: ComponentInstruction): Promise<any> {
		let url = nextInstruction.urlPath;
		this.securityService.isAuthorized(a => {
			if (a && LoggedInRouterOutlet.HOME_ROUTE === url) {
				this.parentRouter.navigateByUrl(LoggedInRouterOutlet.AUTHORIZED_ROUTE);
			} else if (!a && !LoggedInRouterOutlet.PUBLIC_ROUTES[url]) {
				this.parentRouter.navigateByUrl(LoggedInRouterOutlet.HOME_ROUTE);
			}
		});
		return super.activate(nextInstruction);
	}
}