# Guía de Preparación y Pruebas — ContigoApp

Este documento detalla cómo preparar el entorno y realizar las pruebas del sistema, diferenciando entre el desarrollo local y las pruebas de integración con servicios externos como Mercado Pago.

## 1. Local vs. Railway: ¿Cuándo usar cada uno?

Debido a que los **Webhooks de Mercado Pago** requieren una URL pública con HTTPS, el flujo de pagos no se puede probar directamente con el servidor local sin herramientas de túnel adicionales (como ngrok).

| Escenario | Servidor Recomendado | Razón |
| :--- | :--- | :--- |
| **Desarrollo de UI / Layouts** | Local | Rapidez de refresco y depuración inmediata. |
| **Lógica interna / Endpoints nuevos** | Local | No requiere despliegue previo a GitHub. |
| **Pruebas de Pago (Mercado Pago)** | **Railway (Producción)** | Necesario para recibir notificaciones (Webhooks) de pago exitoso. |
| **Suscripciones y Webhooks** | **Railway (Producción)** | Mercado Pago debe poder "ver" tu servidor en internet. |

---

## 2. Configuración del Backend Local (Solo Lógica Interna)

1. **Instalación**:
   ```bash
   cd acompanname-backend
   pip install -r requirements.txt
   ```

2. **Ejecución**:
   ```bash
   python -m uvicorn main:app --host 0.0.0.0 --port 9000 --reload
   ```

3. **Crear Administrador**:
   ```bash
   python create_admin.py
   ```

---

## 3. Pruebas de Mercado Pago (Flujo en Railway)

Para probar que un usuario puede suscribirse y que su cuenta se activa correctamente, sigue estos pasos:

### A. Preparación en Android Studio
Para que la app hable con el servidor que recibe los pagos, debes cambiar la `BASE_URL`:
1. Abre `app/build.gradle.kts`.
2. Cambia `BASE_URL` a tu URL de Railway: 
   `buildConfigField("String", "BASE_URL", "\"https://acompanname-backend-production.up.railway.app/\"")`
3. Sincroniza (Elefante) y haz **Build -> Clean Project**.

### B. Verificación del Webhook
Entra al **Panel de Desarrolladores de Mercado Pago** y asegura que la URL de notificaciones esté configurada así:
- **URL**: `https://tu-app-en-railway.app/payments/webhook`
- **Eventos**: Selecciona "Suscripciones" (subscription_preapproval y subscription_authorized_payment).

### C. Ejecución de la Transacción de $10 MXN
1. **Login**: Entra a la app con un usuario de prueba (Paciente).
2. **Suscripción**: Ve a la sección de suscripción y selecciona el plan **Premium ($10 MXN)**.
3. **Pago**: Se abrirá una Custom Tab de Mercado Pago. Usa una **Tarjeta de Prueba** (disponibles en la doc oficial de MP).
4. **Retorno**: Tras pagar, serás redirigido a una página de "Listo". Cierra la ventana y vuelve a la app.
5. **Confirmación**: 
   - El backend recibirá el webhook y actualizará `subscriptions` y `users` en Supabase.
   - En la app, el estado debería cambiar a "Premium" tras unos segundos.

### D. ¿Cómo verificar que funcionó?
- **Logs de Railway**: Revisa que aparezca un POST a `/payments/webhook` con status `200`.
- **Supabase**: 
  - Tabla `subscriptions`: El registro debe tener `status: authorized`.
  - Tabla `users`: El campo `subscription_plan` debe ser `premium`.

---

## 4. Checklist de Variables en Railway (Entorno de Producción)

Asegúrate de que estas variables estén configuradas en el dashboard de Railway para que el flujo no falle:

- `MP_ACCESS_TOKEN`: Tu Access Token de producción o prueba.
- `MP_PLAN_PREMIUM_ID`: El ID generado al correr `crear_planes.py`.
- `MP_BACK_URL`: `https://tu-app-en-railway.app/payments/return`
- `SUPABASE_URL` y `SUPABASE_KEY`: Credenciales de tu base de datos.
- `JWT_SECRET`: La misma llave que usas localmente para validar tokens.

---

## 5. Verificación de Conexión Local (Legacy)
Si estás probando lógica interna localmente:
- URL: `http://<TU_IP_LOCAL>:9000/ping`
- Asegúrate de que el Firewall de Windows permita el puerto 9000.
