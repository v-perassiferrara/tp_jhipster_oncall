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

describe('TarifaDeServicio e2e test', () => {
  const tarifaDeServicioPageUrl = '/tarifa-de-servicio';
  let username: string;
  let password: string;
  // const tarifaDeServicioSample = {"costoPorMinuto":24369.39,"vigenteDesde":"2023-12-04T09:15:37.586Z"};

  let tarifaDeServicio;
  // let servicio;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"tensely self-assured of","descripcion":"boyfriend","criticidad":"TIER2","entorno":"PRODUCCION","repositorioUrl":"abseil","activo":false},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/tarifa-de-servicios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tarifa-de-servicios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tarifa-de-servicios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

  });
   */

  afterEach(() => {
    if (tarifaDeServicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tarifa-de-servicios/${tarifaDeServicio.id}`,
      }).then(() => {
        tarifaDeServicio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (servicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicios/${servicio.id}`,
      }).then(() => {
        servicio = undefined;
      });
    }
  });
   */

  it('TarifaDeServicios menu should load TarifaDeServicios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tarifa-de-servicio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TarifaDeServicio').should('exist');
    cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
  });

  describe('TarifaDeServicio page', () => {
    it('should have translated page title', () => {
      cy.visit(tarifaDeServicioPageUrl);
      cy.getEntityHeading('TarifaDeServicio').should('not.contain', 'oncallApp.tarifaDeServicio.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tarifaDeServicioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TarifaDeServicio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${tarifaDeServicioPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TarifaDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tarifa-de-servicios',
          body: {
            ...tarifaDeServicioSample,
            servicio: servicio,
          },
        }).then(({ body }) => {
          tarifaDeServicio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tarifa-de-servicios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tarifaDeServicio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tarifaDeServicioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tarifaDeServicioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TarifaDeServicio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tarifaDeServicio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
      });

      it('edit button click should load edit TarifaDeServicio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TarifaDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
      });

      it('edit button click should load edit TarifaDeServicio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TarifaDeServicio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of TarifaDeServicio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tarifaDeServicio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', tarifaDeServicioPageUrl);

        tarifaDeServicio = undefined;
      });
    });
  });

  describe('new TarifaDeServicio page', () => {
    beforeEach(() => {
      cy.visit(tarifaDeServicioPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TarifaDeServicio');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of TarifaDeServicio', () => {
      cy.get(`[data-cy="costoPorMinuto"]`).type('14451.89');
      cy.get(`[data-cy="costoPorMinuto"]`).should('have.value', '14451.89');

      cy.get(`[data-cy="vigenteDesde"]`).type('2023-12-04T14:04');
      cy.get(`[data-cy="vigenteDesde"]`).blur();
      cy.get(`[data-cy="vigenteDesde"]`).should('have.value', '2023-12-04T14:04');

      cy.get(`[data-cy="vigenteHasta"]`).type('2023-12-04T17:45');
      cy.get(`[data-cy="vigenteHasta"]`).blur();
      cy.get(`[data-cy="vigenteHasta"]`).should('have.value', '2023-12-04T17:45');

      cy.get(`[data-cy="servicio"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        tarifaDeServicio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', tarifaDeServicioPageUrl);
    });
  });
});
