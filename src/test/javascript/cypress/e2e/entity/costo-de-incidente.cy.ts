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

describe('CostoDeIncidente e2e test', () => {
  const costoDeIncidentePageUrl = '/costo-de-incidente';
  let username: string;
  let password: string;
  // const costoDeIncidenteSample = {"minutosCaidos":29890,"montoTotal":9806.35};

  let costoDeIncidente;
  // let incidente;
  // let servicio;
  // let periodoDeFacturacion;

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
      url: '/api/incidentes',
      body: {"titulo":"consequently","descripcion":"cavernous throughout favorite","severidad":"SEV3","estado":"RESUELTO","detectadoEn":"2023-12-04T10:41:01.847Z","reconocidoEn":"2023-12-04T05:49:30.876Z","mitigadoEn":"2023-12-04T15:18:34.373Z","resueltoEn":"2023-12-03T23:01:17.711Z","usuariosAfectados":28434,"cumplioObjetivo":true},
    }).then(({ body }) => {
      incidente = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"damaged","descripcion":"ew fervently during","criticidad":"TIER1","entorno":"PRODUCCION","repositorioUrl":"anenst sign","activo":false},
    }).then(({ body }) => {
      servicio = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/periodo-de-facturacions',
      body: {"fechaInicio":"2023-12-04","fechaFin":"2023-12-04","cerrado":false},
    }).then(({ body }) => {
      periodoDeFacturacion = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/costo-de-incidentes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/costo-de-incidentes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/costo-de-incidentes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [incidente],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

    cy.intercept('GET', '/api/periodo-de-facturacions', {
      statusCode: 200,
      body: [periodoDeFacturacion],
    });

  });
   */

  afterEach(() => {
    if (costoDeIncidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/costo-de-incidentes/${costoDeIncidente.id}`,
      }).then(() => {
        costoDeIncidente = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (incidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidentes/${incidente.id}`,
      }).then(() => {
        incidente = undefined;
      });
    }
    if (servicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicios/${servicio.id}`,
      }).then(() => {
        servicio = undefined;
      });
    }
    if (periodoDeFacturacion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/periodo-de-facturacions/${periodoDeFacturacion.id}`,
      }).then(() => {
        periodoDeFacturacion = undefined;
      });
    }
  });
   */

  it('CostoDeIncidentes menu should load CostoDeIncidentes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('costo-de-incidente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CostoDeIncidente').should('exist');
    cy.location('pathname').should('eq', costoDeIncidentePageUrl);
  });

  describe('CostoDeIncidente page', () => {
    it('should have translated page title', () => {
      cy.visit(costoDeIncidentePageUrl);
      cy.getEntityHeading('CostoDeIncidente').should('not.contain', 'oncallApp.costoDeIncidente.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(costoDeIncidentePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CostoDeIncidente page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${costoDeIncidentePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CostoDeIncidente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', costoDeIncidentePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/costo-de-incidentes',
          body: {
            ...costoDeIncidenteSample,
            incidente: incidente,
            servicio: servicio,
            periodo: periodoDeFacturacion,
          },
        }).then(({ body }) => {
          costoDeIncidente = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/costo-de-incidentes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [costoDeIncidente],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(costoDeIncidentePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(costoDeIncidentePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CostoDeIncidente page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('costoDeIncidente');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', costoDeIncidentePageUrl);
      });

      it('edit button click should load edit CostoDeIncidente page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CostoDeIncidente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', costoDeIncidentePageUrl);
      });

      it('edit button click should load edit CostoDeIncidente page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CostoDeIncidente');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', costoDeIncidentePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CostoDeIncidente', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('costoDeIncidente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', costoDeIncidentePageUrl);

        costoDeIncidente = undefined;
      });
    });
  });

  describe('new CostoDeIncidente page', () => {
    beforeEach(() => {
      cy.visit(costoDeIncidentePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CostoDeIncidente');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CostoDeIncidente', () => {
      cy.get(`[data-cy="minutosCaidos"]`).type('15687');
      cy.get(`[data-cy="minutosCaidos"]`).should('have.value', '15687');

      cy.get(`[data-cy="montoTotal"]`).type('10444.61');
      cy.get(`[data-cy="montoTotal"]`).should('have.value', '10444.61');

      cy.get(`[data-cy="calculadoEn"]`).type('2023-12-04T01:39');
      cy.get(`[data-cy="calculadoEn"]`).blur();
      cy.get(`[data-cy="calculadoEn"]`).should('have.value', '2023-12-04T01:39');

      cy.get(`[data-cy="incidente"]`).select(1);
      cy.get(`[data-cy="servicio"]`).select(1);
      cy.get(`[data-cy="periodo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        costoDeIncidente = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', costoDeIncidentePageUrl);
    });
  });
});
