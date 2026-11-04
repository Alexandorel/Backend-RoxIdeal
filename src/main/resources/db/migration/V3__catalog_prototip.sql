DELETE FROM products;
DELETE FROM categories;

ALTER TABLE categories
    ADD COLUMN slug  VARCHAR(255) NOT NULL,
    ADD COLUMN title VARCHAR(255) NOT NULL,
    ADD CONSTRAINT uk_categories_slug UNIQUE (slug);

ALTER TABLE products
    ALTER COLUMN price DROP NOT NULL,
    ADD COLUMN sku      VARCHAR(255) NOT NULL,
    ADD COLUMN brand    VARCHAR(255),
    ADD COLUMN finish   VARCHAR(255),
    ADD COLUMN tag      VARCHAR(255),
    ADD COLUMN spec     VARCHAR(255),
    ADD COLUMN coverage NUMERIC(6, 3),
    ADD COLUMN care     VARCHAR(2000),
    ADD COLUMN swatch   VARCHAR(255),
    ADD CONSTRAINT uk_products_sku UNIQUE (sku);

CREATE TABLE product_specs (
    product_id BIGINT       NOT NULL,
    position   INTEGER      NOT NULL,
    spec_label VARCHAR(255) NOT NULL,
    spec_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, position),
    CONSTRAINT fk_product_specs_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

INSERT INTO categories (slug, name, title) VALUES
    ('riflaje',   'Riflaje',              'Riflaje și panouri decorative'),
    ('sanmarco',  'San Marco',            'Finisaje San Marco'),
    ('accesorii', 'Profile și accesorii', 'Profile și accesorii de montaj');

INSERT INTO products (sku, name, brand, description, spec, price, stock, unit, finish, tag, coverage, swatch, category_id)
SELECT v.sku, v.name, v.brand, v.description, v.spec, v.price, v.stock, 'BUC', v.finish, v.tag, v.coverage, v.swatch, c.id
FROM (VALUES
    ('RX-R101', 'riflaje',   'Panou riflat WPC — Stejar natur',       'Riflaje',          'Panou riflat din compozit lemn-polimer, rezistent la umiditate. Se montează cu adeziv sau pe profile, pe perete sau tavan.', '2900 × 122 × 12 mm',        89,   50, 'lemn',      NULL,        0.354, 'sw-oak'),
    ('RX-R102', 'riflaje',   'Panou riflat MDF — Nuc închis',         'Riflaje',          'Panou riflat din MDF cu folie decorativă nuc, pentru pereți de accent în living și dormitor.',                                '2800 × 120 × 12 mm',        79,   50, 'lemn',      NULL,        0.336, 'sw-walnut'),
    ('RX-A201', 'riflaje',   'Panou riflat acustic — Negru grafit',   'Riflaje acustice', 'Lamele pe suport de fetru fonoabsorbant. Reduce ecoul în birouri, săli de ședință și living-uri deschise.',                   '2400 × 600 mm, fetru 9 mm', 349,  50, 'negru',     'Acustic',   1.44,  'sw-black'),
    ('RX-A202', 'riflaje',   'Panou riflat acustic — Stejar',         'Riflaje acustice', 'Lamele cu finisaj stejar pe fetru negru. Combină căldura lemnului cu performanța acustică.',                                 '2400 × 600 mm, fetru 9 mm', 349,  0,  'lemn',      'Acustic',   1.44,  'sw-oakfelt'),
    ('RX-R103', 'riflaje',   'Panou riflat WPC — Alb mat',            'Riflaje',          'Panou riflat alb mat, ușor de vopsit în nuanța dorită. Potrivit pentru spații luminoase și minimaliste.',                   '2900 × 122 × 12 mm',        89,   50, 'alb',       NULL,        0.354, 'sw-white'),
    ('RX-R104', 'riflaje',   'Panou riflat WPC — Gri antracit',       'Riflaje',          'Nuanță de gri închis, potrivită pentru birouri, recepții și interioare industriale.',                                        '2900 × 122 × 12 mm',        89,   50, 'gri',       'Nou',       0.354, 'sw-grey'),
    ('SM-D301', 'sanmarco',  'Marmorino — tencuială decorativă',      'San Marco',        'Tencuială decorativă pe bază de var cu efect de piatră lustruită, pentru pereți interiori.',                                 'Efect stuc venețian',       NULL, 50, 'decorativ', 'San Marco', NULL,  'sw-marmo'),
    ('SM-D302', 'sanmarco',  'Cadoro — finisaj decorativ catifelat',  'San Marco',        'Finisaj decorativ cu reflexii metalizate și textură catifelată, pentru pereți de accent.',                                   'Efect metalizat',           NULL, 50, 'decorativ', 'San Marco', NULL,  'sw-cadoro'),
    ('SM-V303', 'sanmarco',  'Vopsea lavabilă de interior',           'San Marco',        'Vopsea lavabilă pentru pereți și tavane interioare, colorabilă în nuanțele paletarului San Marco.',                          'Mată, colorabilă',          NULL, 50, 'alb',       NULL,        NULL,  'sw-paint'),
    ('SM-G304', 'sanmarco',  'Grund de amorsare pentru interior',     'San Marco',        'Grund care uniformizează absorbția suportului înainte de vopsele și tencuieli decorative.',                                  'Pregătire suport',          NULL, 50, 'alb',       NULL,        NULL,  'sw-primer'),
    ('RX-P401', 'accesorii', 'Profil de finisaj aluminiu — Negru',    'Accesorii',        'Profil de capăt și colț pentru marginile panourilor riflate. Aluminiu vopsit în câmp electrostatic.',                       'L 2700 mm',                 45,   50, 'negru',     NULL,        NULL,  'sw-profile'),
    ('RX-P402', 'accesorii', 'Adeziv de montaj pentru riflaje',       'Accesorii',        'Adeziv polimeric pentru lipirea panourilor riflate pe gips-carton, beton sau lemn.',                                         'Cartuș 290 ml',             32,   50, 'alb',       NULL,        NULL,  'sw-glue'),
    ('RX-P403', 'accesorii', 'Clipsuri de montaj ascuns',             'Accesorii',        'Clipsuri pentru prinderea mecanică a panourilor pe șipci, fără șuruburi vizibile.',                                          'Set [NR] buc',              NULL, 50, 'gri',       NULL,        NULL,  'sw-clips')
) AS v(sku, category, name, brand, description, spec, price, stock, finish, tag, coverage, swatch)
JOIN categories c ON c.slug = v.category;

UPDATE products p
SET care = 'Panourile se aclimatizează 48 h în încăpere înainte de montaj. Se lipesc cu adeziv polimeric sau se prind pe șipci, cu profile de finisaj la capete. Se curăță cu o lavetă umedă, fără solvenți.'
FROM categories c
WHERE c.id = p.category_id AND c.slug IN ('riflaje', 'accesorii');

UPDATE products p
SET care = 'Suportul trebuie să fie curat, uscat și amorsat cu grundul recomandat. Aplicarea se face conform fișei tehnice San Marco. Pentru efecte decorative recomandăm aplicatori specializați — echipa Roxid Montaje te poate ajuta.'
FROM categories c
WHERE c.id = p.category_id AND c.slug = 'sanmarco';

INSERT INTO product_specs (product_id, position, spec_label, spec_value)
SELECT p.id, v.position, v.label, v.value
FROM (VALUES
    ('RX-R101', 0, 'Dimensiuni', '2900 × 122 × 12 mm'), ('RX-R101', 1, 'Material', 'WPC'),             ('RX-R101', 2, 'Finisaj', 'Stejar natur'),      ('RX-R101', 3, 'Acoperire / buc', '0,354 m²'),
    ('RX-R102', 0, 'Dimensiuni', '2800 × 120 × 12 mm'), ('RX-R102', 1, 'Material', 'MDF foliat'),      ('RX-R102', 2, 'Finisaj', 'Nuc închis'),        ('RX-R102', 3, 'Acoperire / buc', '0,336 m²'),
    ('RX-A201', 0, 'Dimensiuni', '2400 × 600 mm'),      ('RX-A201', 1, 'Suport', 'Fetru PET 9 mm'),    ('RX-A201', 2, 'Finisaj', 'Negru grafit'),      ('RX-A201', 3, 'Clasă absorbție', '[CLASĂ]'),
    ('RX-A202', 0, 'Dimensiuni', '2400 × 600 mm'),      ('RX-A202', 1, 'Suport', 'Fetru PET 9 mm'),    ('RX-A202', 2, 'Finisaj', 'Stejar'),            ('RX-A202', 3, 'Clasă absorbție', '[CLASĂ]'),
    ('RX-R103', 0, 'Dimensiuni', '2900 × 122 × 12 mm'), ('RX-R103', 1, 'Material', 'WPC'),             ('RX-R103', 2, 'Finisaj', 'Alb mat'),           ('RX-R103', 3, 'Acoperire / buc', '0,354 m²'),
    ('RX-R104', 0, 'Dimensiuni', '2900 × 122 × 12 mm'), ('RX-R104', 1, 'Material', 'WPC'),             ('RX-R104', 2, 'Finisaj', 'Gri antracit'),      ('RX-R104', 3, 'Acoperire / buc', '0,354 m²'),
    ('SM-D301', 0, 'Ambalare', '[KG / GĂLEATĂ]'),       ('SM-D301', 1, 'Consum', '[KG / MP]'),         ('SM-D301', 2, 'Aplicare', 'Gletieră inox'),    ('SM-D301', 3, 'Nuanțe', 'Paletar San Marco'),
    ('SM-D302', 0, 'Ambalare', '[L / GĂLEATĂ]'),        ('SM-D302', 1, 'Consum', '[L / MP]'),          ('SM-D302', 2, 'Aplicare', 'Pensulă / gletieră'), ('SM-D302', 3, 'Nuanțe', 'Paletar San Marco'),
    ('SM-V303', 0, 'Ambalare', '[L / GĂLEATĂ]'),        ('SM-V303', 1, 'Randament', '[MP / L]'),       ('SM-V303', 2, 'Aspect', 'Mat'),                ('SM-V303', 3, 'Nuanțe', 'Paletar San Marco'),
    ('SM-G304', 0, 'Ambalare', '[L / GĂLEATĂ]'),        ('SM-G304', 1, 'Randament', '[MP / L]'),       ('SM-G304', 2, 'Diluare', 'Conform fișei tehnice'), ('SM-G304', 3, 'Interior', 'Da'),
    ('RX-P401', 0, 'Lungime', '2700 mm'),               ('RX-P401', 1, 'Material', 'Aluminiu'),        ('RX-P401', 2, 'Culoare', 'Negru mat'),         ('RX-P401', 3, 'Compatibil', 'Riflaje 12 mm'),
    ('RX-P402', 0, 'Volum', '290 ml'),                  ('RX-P402', 1, 'Tip', 'Polimer MS'),           ('RX-P402', 2, 'Consum orientativ', '[BUC / MP]'), ('RX-P402', 3, 'Interior', 'Da'),
    ('RX-P403', 0, 'Conținut', '[NR] buc'),             ('RX-P403', 1, 'Material', 'Oțel zincat'),     ('RX-P403', 2, 'Compatibil', 'Riflaje WPC'),    ('RX-P403', 3, 'Interior', 'Da')
) AS v(sku, position, label, value)
JOIN products p ON p.sku = v.sku;
