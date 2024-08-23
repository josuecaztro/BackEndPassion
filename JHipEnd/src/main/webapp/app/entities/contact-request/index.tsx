import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContactRequest from './contact-request';
import ContactRequestDetail from './contact-request-detail';
import ContactRequestUpdate from './contact-request-update';
import ContactRequestDeleteDialog from './contact-request-delete-dialog';

const ContactRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContactRequest />} />
    <Route path="new" element={<ContactRequestUpdate />} />
    <Route path=":id">
      <Route index element={<ContactRequestDetail />} />
      <Route path="edit" element={<ContactRequestUpdate />} />
      <Route path="delete" element={<ContactRequestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContactRequestRoutes;
