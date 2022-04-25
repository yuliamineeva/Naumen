-- Добавляем админа по умолчанию с одинаковым логином и паролем
INSERT INTO public.user_table(id, active, email, password, username)
SELECT COALESCE((SELECT MAX(id) + 1 FROM public.user_table),1) AS id,
'TRUE', 'admin@javakids.ru', '$2a$10$sTxqUmqAl0OLHCo/vUXVXO69A0UWH1mMtByR.XnHZaMD7yS3ijDCe', 'admin'
WHERE NOT EXISTS (SELECT id
	FROM public.user_table
	WHERE username = 'admin' LIMIT 1);

INSERT INTO public.user_role
SELECT (SELECT id FROM public.user_table WHERE username = 'admin') AS id, 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT user_id FROM public.user_role WHERE user_id = (SELECT id FROM public.user_table WHERE username = 'admin' LIMIT 1));

-- Добавляем лекции, если их нет
INSERT INTO public.lectures(id, content, topic)
SELECT 1, 'Содержимое лекции 1',  'Знакомство с Java'
WHERE NOT EXISTS (SELECT id	FROM public.lectures WHERE id = 1 OR topic = 'Знакомство с Java' LIMIT 1);

INSERT INTO public.lectures(id, content, topic)
SELECT 2, 'Содержимое лекции 2',  'Структура Java приложения'
WHERE NOT EXISTS (SELECT id	FROM public.lectures WHERE id = 2 OR topic = 'Структура Java приложения' LIMIT 1);

INSERT INTO public.lectures(id, content, topic)
SELECT 3, 'Содержимое лекции 3',  'Объявление переменных'
WHERE NOT EXISTS (SELECT id	FROM public.lectures WHERE id = 3 OR topic = 'Объявление переменных' LIMIT 1);
