SELECT age, faculty_id, s.id, s.name, f.name
	FROM public.student s
	JOIN public.faculty f ON s.faculty_id = f.id

SELECT age, faculty_id, s.id, s.name
	FROM public.student s
	JOIN public.avatar a ON s.id = a.student_id
	WHERE a.data IS NOT NULL
