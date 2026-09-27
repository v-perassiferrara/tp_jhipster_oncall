import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PeriodoDeFacturacion e2e test', () => {
  const periodoDeFacturacionPageUrl = '/periodo-de-facturacion';
  let username: string;
  let password: string;
  const periodoDeFacturacionSample = { fechaInicio: '2023-12-04', fechaFin: '2023-12-04', cerrado: true };

  let periodoDeFacturacion;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/periodo-de-facturacions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/periodo-de-facturacions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/periodo-de-facturacions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (periodoDeFacturacion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/periodo-de-facturacions/${periodoDeFacturacion.id}`,
      }).then(() => {
        periodoDeFacturacion = undefined;
      });
    }
  });

  it('PeriodoDeFacturacions menu should load PeriodoDeFacturacions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('periodo-de-facturacion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PeriodoDeFacturacion').should('exist');
    cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
  });

  describe('PeriodoDeFacturacion page', () => {
    it('should have translated page title', () => {
      cy.visit(periodoDeFacturacionPageUrl);
      cy.getEntityHeading('PeriodoDeFacturacion').should('not.contain', 'oncallApp.periodoDeFacturacion.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(periodoDeFacturacionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PeriodoDeFacturacion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${periodoDeFacturacionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PeriodoDeFacturacion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/periodo-de-facturacions',
          body: periodoDeFacturacionSample,
        }).then(({ body }) => {
          periodoDeFacturacion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/periodo-de-facturacions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [periodoDeFacturacion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(periodoDeFacturacionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PeriodoDeFacturacion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('periodoDeFacturacion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
      });

      it('edit button click should load edit PeriodoDeFacturacion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PeriodoDeFacturacion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
      });

      it('edit button click should load edit PeriodoDeFacturacion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PeriodoDeFacturacion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
      });

      it('last delete button click should delete instance of PeriodoDeFacturacion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('periodoDeFacturacion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);

        periodoDeFacturacion = undefined;
      });
    });
  });

  describe('new PeriodoDeFacturacion page', () => {
    beforeEach(() => {
      cy.visit(periodoDeFacturacionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PeriodoDeFacturacion');
    });

    it('should create an instance of PeriodoDeFacturacion', () => {
      cy.get(`[data-cy="fechaInicio"]`).type('2023-12-04');
      cy.get(`[data-cy="fechaInicio"]`).blur();
      cy.get(`[data-cy="fechaInicio"]`).should('have.value', '2023-12-04');

      cy.get(`[data-cy="fechaFin"]`).type('2023-12-04');
      cy.get(`[data-cy="fechaFin"]`).blur();
      cy.get(`[data-cy="fechaFin"]`).should('have.value', '2023-12-04');

      cy.get(`[data-cy="cerrado"]`).should('not.be.checked');
      cy.get(`[data-cy="cerrado"]`).click();
      cy.get(`[data-cy="cerrado"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        periodoDeFacturacion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', periodoDeFacturacionPageUrl);
    });
  });
});
