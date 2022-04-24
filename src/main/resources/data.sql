-- Добавляем админа по умолчанию с одинаковым логином и паролем
INSERT INTO public.user_table(id, active, email, password, username)
SELECT COALESCE((SELECT MAX(id) + 1 FROM public.user_table),1) AS id,
'TRUE', 'admin@javakids.ru', '$2a$10$sTxqUmqAl0OLHCo/vUXVXO69A0UWH1mMtByR.XnHZaMD7yS3ijDCe', 'admin'
WHERE NOT EXISTS (SELECT id
	FROM public.user_table
	WHERE username = 'admin' LIMIT 1);

INSERT INTO public.user_role
SELECT (SELECT id FROM public.user_table WHERE username = 'admin') AS id, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT user_id FROM public.user_role WHERE user_id = (SELECT id FROM public.user_table WHERE username = 'admin'));


/*
INSERT INTO public.lectures(
	id, content, topic)
	VALUES (1, 'Содержимое лекции 1',  'Знакомство с Java');

INSERT INTO public.lectures(
	id, content, topic)
	VALUES (2, 'Содержимое лекции 2', 'Структура Java приложения');

INSERT INTO public.lectures(
	id, content, topic)
	VALUES (3, 'Содержимое лекции 3', 'Объявление переменных');
*/